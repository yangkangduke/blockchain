package com.seeds.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seeds.admin.dto.request.PageReq;
import com.seeds.admin.dto.request.SysNftPicUpHisReq;
import com.seeds.admin.dto.response.SysNftPicUpHisResp;
import com.seeds.admin.entity.SysNftPicEntity;
import com.seeds.admin.entity.SysNftPicUpHisEntity;
import com.seeds.admin.exceptions.GenericException;
import com.seeds.admin.mapper.SysNftPicUpHisMapper;
import com.seeds.admin.service.SysFileService;
import com.seeds.admin.service.SysNftPicService;
import com.seeds.admin.service.SysNftPicUpHisService;
import com.seeds.common.web.context.UserContext;
import com.seeds.common.web.oss.FileProperties;
import com.seeds.common.web.oss.FileTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * NFT内容上传记录服务实现类
 *
 * @author yang.deng
 * @date 2023/2/20
 */

@Service
@Slf4j
public class SysNftPicUpHisServiceImpl extends ServiceImpl<SysNftPicUpHisMapper, SysNftPicUpHisEntity> implements SysNftPicUpHisService {
    @Autowired
    private FileTemplate template;

    @Autowired
    private FileProperties properties;

    @Autowired
    private SysNftPicService sysNftPicService;

    @Autowired
    private SysFileService sysFileService;

    @Override
    public IPage<SysNftPicUpHisResp> queryPage(PageReq req) {
        LambdaQueryWrapper<SysNftPicUpHisEntity> queryWrapper = new QueryWrapper<SysNftPicUpHisEntity>().lambda()
                .orderByDesc(SysNftPicUpHisEntity::getCreatedAt);

        Page<SysNftPicUpHisEntity> page = new Page<>(req.getCurrent(), req.getSize());
        List<SysNftPicUpHisEntity> records = this.page(page, queryWrapper).getRecords();
        if (CollectionUtils.isEmpty(records)) {
            return page.convert(p -> null);
        }
        return page.convert(p -> {
            SysNftPicUpHisResp resp = new SysNftPicUpHisResp();
            BeanUtils.copyProperties(p, resp);
            return resp;
        });
    }

    @Override
    @Transactional
    public void upload(MultipartFile[] files, SysNftPicUpHisReq req) {

        // 记录上传历史
        SysNftPicUpHisEntity hisEntity = new SysNftPicUpHisEntity();
        BeanUtils.copyProperties(req, hisEntity);
        hisEntity.setCreatedBy(UserContext.getCurrentAdminUserId());
        hisEntity.setCreatedAt(System.currentTimeMillis());
        hisEntity.setUpdatedBy(UserContext.getCurrentAdminUserId());
        hisEntity.setUpdatedAt(System.currentTimeMillis());
        this.save(hisEntity);

        List<SysNftPicEntity> picInsert = new ArrayList<>();
        List<SysNftPicEntity> picUpdate = new ArrayList<>();
        for (MultipartFile file : files) {
            // 上传文件到oss
            String bucketName = properties.getBucketName();
            String originalFilename = file.getOriginalFilename();
            String objectName = "NFT_PIC/" + originalFilename;

            try (InputStream inputStream = file.getInputStream()) {
                template.putObject(bucketName, objectName, inputStream, file.getContentType());

                // 记录每张图片
                SysNftPicEntity sysNftPicEntity = new SysNftPicEntity();
                BeanUtils.copyProperties(req, sysNftPicEntity);
                sysNftPicEntity.setPicName(originalFilename);
                sysNftPicEntity.setHisId(hisEntity.getId());
                sysNftPicEntity.setUrl(sysFileService.getFileUrl(objectName));
                sysNftPicEntity.setCreatedBy(UserContext.getCurrentAdminUserId());
                sysNftPicEntity.setCreatedAt(System.currentTimeMillis());
                sysNftPicEntity.setUpdatedBy(UserContext.getCurrentAdminUserId());
                sysNftPicEntity.setUpdatedAt(System.currentTimeMillis());

                SysNftPicEntity one = sysNftPicService.getOne(new LambdaQueryWrapper<SysNftPicEntity>().eq(SysNftPicEntity::getPicName, originalFilename));
                if (Objects.isNull(one)) {
                    picInsert.add(sysNftPicEntity);
                } else {
                    sysNftPicEntity.setId(one.getId());
                    picUpdate.add(sysNftPicEntity);
                }


            } catch (Exception e) {
                log.error("文件上传失败，fileName={}", originalFilename);
                throw new GenericException("File upload failed");
            }
        }
        sysNftPicService.saveBatch(picInsert);
        sysNftPicService.updateBatchById(picUpdate);
    }

}
