package com.seeds.uc.web.register.service.impl;

import com.seeds.uc.dto.redis.AuthCode;
import com.seeds.uc.dto.request.EmailCodeSendReq;
import com.seeds.uc.dto.request.EmailCodeVerifyReq;
import com.seeds.uc.enums.AuthCodeUseTypeEnum;
import com.seeds.uc.enums.ClientAuthTypeEnum;
import com.seeds.uc.enums.UcErrorCode;
import com.seeds.uc.exceptions.LoginException;
import com.seeds.uc.mapper.SecurityStrategyMapper;
import com.seeds.uc.mapper.UserMapper;
import com.seeds.uc.service.*;
import com.seeds.uc.web.register.service.RegisterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author yk
 * @email 819628513@qq.com
 * @date 2022/07/13
 */
@Slf4j
@Service
public class RegisterServiceImpl implements RegisterService {
    @Autowired
    private UserService userService;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private CacheService cacheService;
    @Autowired
    private SendCodeService sendCodeService;
    @Autowired
    private RiskControlService riskControlService;
    @Autowired
    private SecurityStrategyMapper securityStrategyMapper;
    @Autowired
    private GoogleAuthService googleAuthService;

    /**
     * 发送邮箱验证码
     *
     * @param sendReq
     */
    @Override
    public void sendEmailCode(EmailCodeSendReq sendReq) {
        log.info("AuthController - sendEmailCode got request: {}", sendReq);
        if (AuthCodeUseTypeEnum.CODE_NO_NEED_LOGIN_READ_REQUEST.contains(sendReq.getUseType())) {
            // 不需要登陆，请求里带邮箱，如: REGISTER
            sendCodeService.sendEmailWithUseType(sendReq.getEmail(), sendReq.getUseType());
        }
    }

    /**
     * 邮箱验证码校验
     *
     * @param verifyReq
     */
    @Override
    public void verifyRegisterCode(EmailCodeVerifyReq verifyReq) {
        String email = verifyReq.getEmail();
        String code = verifyReq.getCode();
        AuthCode authCode = cacheService.getAuthCode(email, verifyReq.getUseType(), ClientAuthTypeEnum.EMAIL);
        if (authCode == null || authCode.getCode() == null || !authCode.getCode().equals(code)) {
            throw new LoginException(UcErrorCode.ERR_10033_WRONG_EMAIL_CODE);
        }
        // todo 需要添加数据到数据库
    }

}
