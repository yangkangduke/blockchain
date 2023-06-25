package com.seeds.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seeds.admin.dto.request.ListReq;
import com.seeds.admin.dto.request.SysGameVideoAddOrModifyReq;
import com.seeds.admin.dto.request.SysGameVideosReq;
import com.seeds.admin.dto.response.SysGameVideosResp;
import com.seeds.admin.entity.SysGameVideosEntity;
import com.seeds.admin.entity.SysGameVideosTagsEntity;
import com.seeds.admin.enums.AdminErrorCodeEnum;
import com.seeds.admin.enums.WhetherEnum;
import com.seeds.admin.exceptions.GenericException;
import com.seeds.admin.mapper.SysGameVideosMapper;
import com.seeds.admin.service.ISysGameVideosService;
import com.seeds.admin.service.ISysGameVideosTagsService;
import com.seeds.common.web.context.UserContext;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * 游戏视频管理 服务实现类
 * </p>
 *
 * @author hewei
 * @since 2023-06-25
 */
@Service
public class SysGameVideosServiceImpl extends ServiceImpl<SysGameVideosMapper, SysGameVideosEntity> implements ISysGameVideosService {
    @Resource
    private ISysGameVideosTagsService videosTagsService;

    @Override
    public IPage<SysGameVideosResp> queryPage(SysGameVideosReq req) {
        LambdaQueryWrapper<SysGameVideosEntity> wrapper = new LambdaQueryWrapper<SysGameVideosEntity>()
                .like(StringUtils.isNotBlank(req.getTitle()), SysGameVideosEntity::getTitle, req.getTitle())
                .like(StringUtils.isNotBlank(req.getTag()), SysGameVideosEntity::getVideoTagName, req.getTag());

        Page<SysGameVideosEntity> page = new Page<>(req.getCurrent(), req.getSize());
        List<SysGameVideosEntity> records = page(page, wrapper).getRecords();
        if (CollectionUtils.isEmpty(records)) {
            return page.convert(p -> null);
        }
        return page.convert(p -> {
            SysGameVideosResp resp = new SysGameVideosResp();
            BeanUtils.copyProperties(p, resp);
            return resp;
        });
    }

    @Override
    public void add(SysGameVideoAddOrModifyReq req) {
        long topVideos = getTopVideoNum();
        if (topVideos >= 5) {
            throw new GenericException(AdminErrorCodeEnum.ERR_80007_NUMBER_EXCEEDS_THE_TOP);
        }
        SysGameVideosEntity entity = new SysGameVideosEntity();
        if (StringUtils.isNotBlank(req.getVideoTagName())) {
            String videoTags = saveTag(req);
            entity.setVideoTag(videoTags);
        }
        BeanUtils.copyProperties(req, entity);
        entity.setCreatedAt(System.currentTimeMillis());
        entity.setUpdatedAt(System.currentTimeMillis());
        entity.setCreatedBy(UserContext.getCurrentAdminUserId());
        entity.setUpdatedBy(UserContext.getCurrentAdminUserId());
        this.save(entity);
    }

    @Override
    public SysGameVideosResp detail(Long id) {
        SysGameVideosResp resp = new SysGameVideosResp();
        SysGameVideosEntity entity = getById(id);
        if (!ObjectUtils.isEmpty(entity)) {
            BeanUtils.copyProperties(entity, resp);
        }
        return resp;
    }

    @Override
    public void modify(SysGameVideoAddOrModifyReq req) {
        long topVideos = getTopVideoNum();
        if (topVideos >= 5) {
            throw new GenericException(AdminErrorCodeEnum.ERR_80007_NUMBER_EXCEEDS_THE_TOP);
        }
        SysGameVideosEntity entity = new SysGameVideosEntity();
        if (StringUtils.isNotBlank(req.getVideoTagName())) {
            String videoTags = saveTag(req);
            entity.setVideoTag(videoTags);
        }
        BeanUtils.copyProperties(req, entity);
        entity.setUpdatedAt(System.currentTimeMillis());
        entity.setUpdatedBy(UserContext.getCurrentAdminUserId());
        this.updateById(entity);
    }

    @Override
    public void delete(ListReq req) {
        Set<Long> ids = req.getIds();
        removeBatchByIds(ids);
    }

    @Override
    public List<SysGameVideosResp> getTopVideos() {
        LambdaQueryWrapper<SysGameVideosEntity> wrapper = new QueryWrapper<SysGameVideosEntity>().lambda()
                .eq(SysGameVideosEntity::getIsTop, WhetherEnum.YES.value());
        List<SysGameVideosEntity> list = list(wrapper);
        return list.stream().map(p -> {
            SysGameVideosResp resp = new SysGameVideosResp();
            BeanUtils.copyProperties(p, resp);
            return resp;
        }).collect(Collectors.toList());
    }

    private long getTopVideoNum() {
        LambdaQueryWrapper<SysGameVideosEntity> wrapper = new QueryWrapper<SysGameVideosEntity>().lambda()
                .eq(SysGameVideosEntity::getIsTop, WhetherEnum.YES.value());
        return count(wrapper);
    }

    private String saveTag(SysGameVideoAddOrModifyReq req) {
        StringBuilder videoTags = new StringBuilder();
        String[] tags = req.getVideoTagName().split(",");
        for (String tag : tags) {
            SysGameVideosTagsEntity tagsEntity = videosTagsService.getOne(new LambdaQueryWrapper<SysGameVideosTagsEntity>().eq(SysGameVideosTagsEntity::getTName, tag));
            if (null != tagsEntity) {
                videoTags.append(tagsEntity.getId()).append(",");
            } else {
                SysGameVideosTagsEntity videosTagsEntity = new SysGameVideosTagsEntity();
                videosTagsEntity.setTName(tag);
                videosTagsEntity.setCreatedAt(System.currentTimeMillis());
                videosTagsEntity.setUpdatedAt(System.currentTimeMillis());
                videosTagsEntity.setCreatedBy(UserContext.getCurrentAdminUserId());
                videosTagsEntity.setUpdatedBy(UserContext.getCurrentAdminUserId());
                videosTagsService.save(videosTagsEntity);
                videoTags.append(videosTagsEntity.getId()).append(",");
            }
        }
        return videoTags.deleteCharAt(videoTags.length() - 1).toString();
    }
}
