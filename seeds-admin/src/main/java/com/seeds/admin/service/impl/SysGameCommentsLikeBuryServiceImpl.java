package com.seeds.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seeds.admin.dto.request.SysGameCommentsLikeOrBuryReq;
import com.seeds.admin.dto.response.SysGameCommentsLikeOrBuryResp;
import com.seeds.admin.entity.SysGameCommentsEntity;
import com.seeds.admin.entity.SysGameCommentsLikeBuryEntity;
import com.seeds.admin.mapper.SysGameCommentsLikeBuryMapper;
import com.seeds.admin.service.SysGameCommentsLikeBuryService;
import com.seeds.admin.service.SysGameCommentsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @author: hewei
 * @date 2022/8/26
 */

@Service
public class SysGameCommentsLikeBuryServiceImpl extends ServiceImpl<SysGameCommentsLikeBuryMapper, SysGameCommentsLikeBuryEntity> implements SysGameCommentsLikeBuryService {

    @Resource
    private SysGameCommentsService gameCommentsService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SysGameCommentsLikeOrBuryResp like(SysGameCommentsLikeOrBuryReq req) {
        SysGameCommentsLikeOrBuryResp resp = new SysGameCommentsLikeOrBuryResp();
        LambdaQueryWrapper<SysGameCommentsLikeBuryEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysGameCommentsLikeBuryEntity::getCommentsId, req.getCommentsId())
                .eq(SysGameCommentsLikeBuryEntity::getUcUserId, req.getUcUserId())
                .eq(SysGameCommentsLikeBuryEntity::getType, 1);

        SysGameCommentsLikeBuryEntity one = this.getOne(wrapper);
        try {
            if (one == null) {
                // 用户没点过赞，点赞表插入数据
                SysGameCommentsLikeBuryEntity entity = new SysGameCommentsLikeBuryEntity();
                entity.setCommentsId(req.getCommentsId());
                entity.setUcUserId(req.getUcUserId());
                entity.setCreatedBy(req.getUcUserId());
                entity.setUpdatedBy(req.getUcUserId());
                entity.setType(1);
                this.save(entity);
                // 评论表更新点赞数
                LambdaUpdateWrapper<SysGameCommentsEntity> updateWrap = new UpdateWrapper<SysGameCommentsEntity>().lambda()
                        .setSql("`likes`=`likes`+1")
                        .eq(SysGameCommentsEntity::getId, req.getCommentsId());
                gameCommentsService.update(updateWrap);
                resp.setIsLike(1);

            } else {
                // 已经点过赞，再次点击就是取消赞
                this.removeById(one.getId());
                // 评论表更新点赞数
                LambdaUpdateWrapper<SysGameCommentsEntity> updateWrap = new UpdateWrapper<SysGameCommentsEntity>().lambda()
                        .setSql("`likes`=`likes`-1")
                        .eq(SysGameCommentsEntity::getId, req.getCommentsId());
                gameCommentsService.update(updateWrap);
                resp.setIsLike(0);
            }

            LambdaQueryWrapper<SysGameCommentsLikeBuryEntity> buryWrapper = new LambdaQueryWrapper<>();
            buryWrapper.eq(SysGameCommentsLikeBuryEntity::getCommentsId, req.getCommentsId())
                    .eq(SysGameCommentsLikeBuryEntity::getUcUserId, req.getUcUserId())
                    .eq(SysGameCommentsLikeBuryEntity::getType, 2);
            SysGameCommentsLikeBuryEntity bury = this.getOne(buryWrapper);

            if (bury != null) { // 用户踩过，删除踩记录，更新被踩数
                this.removeById(bury.getId());
                // 评论表更新点被踩数
                LambdaUpdateWrapper<SysGameCommentsEntity> updateWrap = new UpdateWrapper<SysGameCommentsEntity>().lambda()
                        .setSql("`bury`=`bury`-1")
                        .eq(SysGameCommentsEntity::getId, req.getCommentsId());
                gameCommentsService.update(updateWrap);
            }
            SysGameCommentsEntity commentsEntity = gameCommentsService.getById(req.getCommentsId());
            resp.setBuryNum(commentsEntity.getBury());
            resp.setLikeNum(commentsEntity.getLikes());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return resp;

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SysGameCommentsLikeOrBuryResp bury(SysGameCommentsLikeOrBuryReq req) {
        SysGameCommentsLikeOrBuryResp resp = new SysGameCommentsLikeOrBuryResp();

        LambdaQueryWrapper<SysGameCommentsLikeBuryEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysGameCommentsLikeBuryEntity::getCommentsId, req.getCommentsId())
                .eq(SysGameCommentsLikeBuryEntity::getUcUserId, req.getUcUserId())
                .eq(SysGameCommentsLikeBuryEntity::getType, 2);
        SysGameCommentsLikeBuryEntity one = this.getOne(wrapper);
        if (one == null) {
            // 用户没踩过
            SysGameCommentsLikeBuryEntity entity = new SysGameCommentsLikeBuryEntity();
            entity.setCommentsId(req.getCommentsId());
            entity.setUcUserId(req.getUcUserId());
            entity.setCreatedBy(req.getUcUserId());
            entity.setUpdatedBy(req.getUcUserId());
            entity.setType(2);
            this.save(entity);
            // 评论表更新 `踩` 数量
            LambdaUpdateWrapper<SysGameCommentsEntity> updateWrap = new UpdateWrapper<SysGameCommentsEntity>().lambda()
                    .setSql("`bury`=`bury`+1")
                    .eq(SysGameCommentsEntity::getId, req.getCommentsId());
            gameCommentsService.update(updateWrap);
            resp.setIsBury(1);
        } else {
            // 已经踩过，再次点击就是取消踩
            this.removeById(one.getId());
            // 评论表更新被踩数
            LambdaUpdateWrapper<SysGameCommentsEntity> updateWrap = new UpdateWrapper<SysGameCommentsEntity>().lambda()
                    .setSql("`bury`=`bury`-1")
                    .eq(SysGameCommentsEntity::getId, req.getCommentsId());
            gameCommentsService.update(updateWrap);
            resp.setIsBury(0);
        }

        LambdaQueryWrapper<SysGameCommentsLikeBuryEntity> likeWrapper = new LambdaQueryWrapper<>();
        likeWrapper.eq(SysGameCommentsLikeBuryEntity::getCommentsId, req.getCommentsId())
                .eq(SysGameCommentsLikeBuryEntity::getUcUserId, req.getUcUserId())
                .eq(SysGameCommentsLikeBuryEntity::getType, 1);

        SysGameCommentsLikeBuryEntity like = this.getOne(likeWrapper);
        if (like != null) {
            this.removeById(like.getId());
            // 评论表更新点赞数
            LambdaUpdateWrapper<SysGameCommentsEntity> updateWrap = new UpdateWrapper<SysGameCommentsEntity>().lambda()
                    .setSql("`likes`=`likes`-1")
                    .eq(SysGameCommentsEntity::getId, req.getCommentsId());
            gameCommentsService.update(updateWrap);
        }
        SysGameCommentsEntity commentsEntity = gameCommentsService.getById(req.getCommentsId());
        resp.setBuryNum(commentsEntity.getBury());
        resp.setLikeNum(commentsEntity.getLikes());
        return resp;
    }
}
