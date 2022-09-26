package com.seeds.notification.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.seeds.notification.dto.NoticeDTO;
import com.seeds.notification.dto.request.NoticePageReq;
import com.seeds.notification.dto.request.NoticeSaveReq;
import com.seeds.notification.dto.response.NotificationResp;
import com.seeds.notification.entity.NotificationEntity;

import java.util.List;

/**
 * @author weihe
 */
public interface NotificationService extends IService<NotificationEntity> {

    IPage<NotificationResp> getNoticeByUserId(NoticePageReq req);


    Boolean updateReadStatus(Long id);


    Boolean saveNotice(NoticeSaveReq req);


    void sendNotice(NoticeDTO Notice);


    Boolean getUnReadNoticeFlag(Long userId);
}
