package com.seeds.uc.web.user.service.impl;

import com.seeds.uc.model.user.entity.UcUser;
import com.seeds.uc.web.user.mapper.UcUserMapper;
import com.seeds.uc.web.user.service.IGoogleAuthService;
import com.warrenstrange.googleauth.GoogleAuthenticator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author yk
 * @email 819628513@qq.com
 * @date 2022/07/15
 */
@Slf4j
@Service
public class GoogleAuthServiceImpl implements IGoogleAuthService {
    @Autowired
    UcUserMapper userMapper;

    @Override
    public String genGaSecret() {
        GoogleAuthenticator ga = new GoogleAuthenticator();
        return ga.createCredentials().getKey();
    }

    @Override
    public boolean verifyUserCode(Long uid, String userInputCode) {
        UcUser user = userMapper.selectById(uid);
        return verify(userInputCode, user.getGaSecret());
    }

    @Override
    public boolean verify(String userInputCode, String userSecret) {
        GoogleAuthenticator googleAuthenticator = new GoogleAuthenticator();
        return googleAuthenticator.authorize(userSecret, Integer.parseInt(userInputCode.replaceFirst("0*", "")));
    }
}