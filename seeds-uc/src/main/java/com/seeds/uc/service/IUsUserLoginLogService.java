package com.seeds.uc.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.seeds.uc.model.UcUserLoginLog;

/**
 * @author: hewei
 * @date 2022/9/28
 */
public interface IUsUserLoginLogService extends IService<UcUserLoginLog> {
    void recordLog(Long ucUserId, String email, String clientIp);

    boolean checkNeed2FA(String email, String clientIp);
}
