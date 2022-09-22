package com.seeds.notification.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seeds.notification.dto.NoticeDTO;
import com.seeds.notification.dto.request.NoticePageReq;
import com.seeds.notification.dto.request.NoticeSaveReq;
import com.seeds.notification.dto.response.NotificationResp;
import com.seeds.notification.entity.NotificationEntity;
import com.seeds.notification.exceptions.GenericException;
import com.seeds.notification.mapper.NotificationMapper;
import com.seeds.notification.server.PushServer;
import com.seeds.notification.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.Date;
import java.util.List;

/**
 * @author weihe
 */
@Service
@Slf4j
public class NotificationServiceImpl extends ServiceImpl<NotificationMapper, NotificationEntity>
        implements NotificationService {

    @Override
    public IPage<NotificationResp> getNoticeByUserId(NoticePageReq req) {
        LambdaQueryWrapper<NotificationEntity> wrapper = new LambdaQueryWrapper<>();

        wrapper.eq(!ObjectUtils.isEmpty(req.getUserId()), NotificationEntity::getUcUserId, req.getUserId());

        Page<NotificationEntity> page = new Page<>(req.getCurrent(), req.getSize());
        List<NotificationEntity> records = page(page, wrapper).getRecords();
        if (CollectionUtils.isEmpty(records)) {
            return page.convert(p -> null);
        }
        return page.convert(p -> {
            NotificationResp resp = new NotificationResp();
            BeanUtils.copyProperties(p, resp);
            return resp;
        });
    }

    @Override
    public Boolean updateReadStatus(Long id) {
        log.info("updateReadStatus - noticeId:{}", id);
        if (!ObjectUtils.isEmpty(id)) {
            NotificationEntity notificationEntity = new NotificationEntity();
            notificationEntity.setHasRead(1);
            notificationEntity.setId(id);
            notificationEntity.setUpdatedAt(new Date().getTime());
            return updateById(notificationEntity);
        }
        throw new GenericException("通知id不能为空");
    }

    @Override
    public Boolean saveNotice(NoticeSaveReq req) {
        NotificationEntity entity = new NotificationEntity();
        BeanUtils.copyProperties(req, entity);
        entity.setCreatedAt(new Date().getTime());
        entity.setUpdatedAt(new Date().getTime());

        return save(entity);
    }

    @Override
    public void sendNotice(NoticeDTO Notice) {
        if (null == Notice) {
            return;
        }
        PushServer.pushServer.push(Notice);
    }

    /**
     * 获取用户是否有未读消息
     *
     * @param userId
     * @return
     */
    @Override
    public Boolean getUnReadNoticeFlag(Long userId) {
        return count(new QueryWrapper<NotificationEntity>().eq("uc_user_id", userId).eq("has_read", 0)) > 0;
    }
}




