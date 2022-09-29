package com.seeds.uc.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seeds.uc.mapper.UcUserLoginLogMapper;
import com.seeds.uc.model.UcUser;
import com.seeds.uc.model.UcUserLoginLog;
import com.seeds.uc.service.IUcUserService;
import com.seeds.uc.service.IUsUserLoginLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author: hewei
 * @date 2022/9/28
 */
@Service
public class UsUserLoginLogService extends ServiceImpl<UcUserLoginLogMapper, UcUserLoginLog> implements IUsUserLoginLogService {

    @Autowired
    private IUcUserService ucUserService;

    @Override
    public void recordLog(Long ucUserId, String email, String clientIp) {
        UcUserLoginLog loginLog = new UcUserLoginLog();
        loginLog.setUcUserId(ucUserId);
        loginLog.setEmail(email);
        loginLog.setLoginIp(clientIp);
        loginLog.setLoginTime(System.currentTimeMillis());
        this.save(loginLog);
    }

    @Override
    public boolean checkNeed2FA(String email, String clientIp) {
        UcUser ucUser = ucUserService.getOne(new QueryWrapper<UcUser>().lambda()
                .eq(UcUser::getEmail, email));
        if (ucUser == null) {
            return false;
        }

        LambdaQueryWrapper<UcUserLoginLog> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UcUserLoginLog::getUcUserId, ucUser.getId())
                .orderByDesc(UcUserLoginLog::getLoginTime).last("limit 1");

        UcUserLoginLog one = this.getOne(queryWrapper);
        if (null != one) {
            return clientIp.equals(one.getLoginIp());
        }
        return false;
    }
}
