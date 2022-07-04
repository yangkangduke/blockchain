package com.seeds.uc.service.impl;

import com.seeds.uc.mapper.UserMapper;
import com.seeds.uc.model.User;
import com.seeds.uc.service.GoogleAuthService;
import com.warrenstrange.googleauth.GoogleAuthenticator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author allen
 * @email allen.hua.ai@gmail.com
 * @date 2020/8/26
 */
@Slf4j
@Service
public class GoogleAuthServiceImpl implements GoogleAuthService {
    @Autowired
    UserMapper userMapper;

    @Override
    public String genGaSecret() {
        GoogleAuthenticator ga = new GoogleAuthenticator();
        return ga.createCredentials().getKey();
    }

    @Override
    public boolean verifyUserCode(Long uid, String userInputCode) {
        User user = userMapper.selectByPrimaryKey(uid);
        return verify(userInputCode, user.getGaSecret());
    }

    @Override
    public boolean verify(String userInputCode, String userSecret) {
        GoogleAuthenticator googleAuthenticator = new GoogleAuthenticator();
        return googleAuthenticator.authorize(userSecret, Integer.parseInt(userInputCode.replaceFirst("0*", "")));
    }
}