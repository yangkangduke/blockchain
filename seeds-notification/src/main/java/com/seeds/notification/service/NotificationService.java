package com.seeds.notification.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.seeds.notification.dto.NotificationDto;
import com.seeds.notification.dto.request.NoticePageReq;
import com.seeds.notification.dto.request.NotificationReq;
import com.seeds.notification.dto.response.NotificationResp;
import com.seeds.notification.entity.NotificationEntity;

/**
 * @author weihe
 */
public interface NotificationService extends IService<NotificationEntity> {

    IPage<NotificationResp> getNoticeByUserId(NoticePageReq req);


    Boolean updateReadStatus(Long id);


    Boolean saveNotice(NotificationReq req);


    void sendNotice(NotificationDto Notice, String userSource);


    Boolean getUnReadNoticeFlag(Long userId, String userSource);

    Boolean delete(Long id);

    Boolean readAll(Long userId, String userSource);

    Boolean deleteAll(Long userId, String userSource);
}