package com.seeds.uc.service;

import com.seeds.uc.dto.InterUserInfo;
import com.seeds.uc.dto.UserDto;
import com.seeds.uc.dto.request.LoginReq;
import com.seeds.uc.dto.request.RegisterReq;
import com.seeds.uc.enums.ClientAuthTypeEnum;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author allen
 * @email allen.hua.ai@gmail.com
 * @date 2020/7/26
 */
public interface UserService {
    // return a token for registration
    UserDto register(ClientAuthTypeEnum authTypeEnum, RegisterReq registerReq);

    UserDto getUserByUcToken(String token);

    UserDto getUserByUid(Long uid);

    // verify if login success
    UserDto verifyLogin(LoginReq loginReq);

    void logout(String token);

    boolean verifyPasswordByUid(Long uid, String rawPassword);

    void setPasswordByUid(Long uid, String newPassword);

    @Transactional
    void freezeUser(Long uid);

    InterUserInfo getInternalUserInfo(Long uid);
}