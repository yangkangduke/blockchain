package com.seeds.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seeds.admin.dto.request.SysGameCommentsRepliesReq;
import com.seeds.admin.dto.response.SysGameCommentsRepliesResp;
import com.seeds.admin.entity.SysGameCommentsEntity;
import com.seeds.admin.entity.SysGameCommentsRepliesEntity;
import com.seeds.admin.mapper.SysGameCommentsRepliesMapper;
import com.seeds.admin.service.SysGameCommentsRepliesService;
import com.seeds.admin.service.SysGameCommentsService;
import org.joda.time.Instant;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author: hewei
 * @date 2022/8/26
 */
@Service
public class SysGameCommentsRepliesServiceImpl extends ServiceImpl<SysGameCommentsRepliesMapper, SysGameCommentsRepliesEntity> implements SysGameCommentsRepliesService {

    @Resource
    private SysGameCommentsService commentsService;

    @Override
    public Integer reply(SysGameCommentsRepliesReq req) {
        SysGameCommentsRepliesEntity entity = new SysGameCommentsRepliesEntity();
        BeanUtils.copyProperties(req, entity);
        entity.setReplyTime(new Instant().getMillis());
        this.save(entity);
        // 评论表更新评论数
        LambdaUpdateWrapper<SysGameCommentsEntity> replyWrap = new UpdateWrapper<SysGameCommentsEntity>().lambda()
                .setSql("`replies`=`replies`+1")
                .eq(SysGameCommentsEntity::getId, req.getCommentsId());
        commentsService.update(replyWrap);
        SysGameCommentsEntity comments = commentsService.getById(req.getCommentsId());
        Integer replies = comments.getReplies();
        return replies;

    }

    @Override
    public List<SysGameCommentsRepliesResp> getReplies(Long commentsId) {
        List<SysGameCommentsRepliesResp> respList = new ArrayList<>();
        LambdaQueryWrapper<SysGameCommentsRepliesEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysGameCommentsRepliesEntity::getCommentsId, commentsId);

        List<SysGameCommentsRepliesEntity> list = this.list(wrapper);

        if (!CollectionUtils.isEmpty(list)) {
            respList = list.stream().map(item -> {
                SysGameCommentsRepliesResp resp = new SysGameCommentsRepliesResp();
                BeanUtils.copyProperties(item, resp);
                return resp;
            }).collect(Collectors.toList());
        }

        return respList;
    }
}
