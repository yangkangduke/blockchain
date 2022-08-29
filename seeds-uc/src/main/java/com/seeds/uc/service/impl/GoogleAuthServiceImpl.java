package com.seeds.uc.service.impl;

import com.seeds.uc.enums.UcErrorCodeEnum;
import com.seeds.uc.exceptions.InvalidArgumentsException;
import com.seeds.uc.mapper.UcUserMapper;
import com.seeds.uc.model.UcUser;
import com.seeds.uc.service.IGoogleAuthService;
import com.warrenstrange.googleauth.GoogleAuthenticator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author yk
 * @email 819628513@qq.com
 * @date 2022/07/15
 */
@Slf4j
@Service
@Transactional
public class GoogleAuthServiceImpl implements IGoogleAuthService {

    @Autowired
    UcUserMapper userMapper;


    @Override
    public String genGaSecret() {
        GoogleAuthenticator ga = new GoogleAuthenticator();
        return ga.createCredentials().getKey();
    }

    @Override
    public Boolean verifyUserCode(Long uid, String userInputCode) {
        UcUser user = userMapper.selectById(uid);
        return verify(userInputCode, user.getGaSecret());
    }

    @Override
    public boolean verify(String userInputCode, String userSecret) {
        boolean authorize;
        try {
            GoogleAuthenticator googleAuthenticator = new GoogleAuthenticator();
            authorize = googleAuthenticator.authorize(userSecret, Integer.parseInt(userInputCode.replaceFirst("0*", "")));
        } catch (Exception e) {
            throw new InvalidArgumentsException(UcErrorCodeEnum.ERR_14000_GA_VERIFICATION_FAILED);
        }
        return authorize;
    }
}