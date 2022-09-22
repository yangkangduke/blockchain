package com.seeds.notification.service;

import com.seeds.notification.dto.NoticeDTO;

/**
 * @author: hewei
 */
public interface IPushService {

    /**
     * 向页面推送消息
     */
    void push(NoticeDTO message);
}