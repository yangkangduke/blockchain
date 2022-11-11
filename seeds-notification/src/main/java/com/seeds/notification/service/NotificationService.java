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


    void sendNotice(NotificationDto Notice);


    Boolean getUnReadNoticeFlag(Long userId);

    Boolean delete(Long id);

    Boolean readAll(Long userId);

    Boolean deleteAll(Long userId);
}
