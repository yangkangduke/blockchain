package com.seeds.uc.service.impl;

import com.seeds.uc.dto.InterUserInfo;
import com.seeds.uc.dto.UserDto;
import com.seeds.uc.dto.mapstruct.UserDtoMapper;
import com.seeds.uc.dto.redis.LoginUser;
import com.seeds.uc.dto.request.LoginReq;
import com.seeds.uc.dto.request.RegisterReq;
import com.seeds.uc.enums.ClientAuthTypeEnum;
import com.seeds.uc.enums.ClientStateEnum;
import com.seeds.uc.enums.ClientTypeEnum;
import com.seeds.uc.enums.UcErrorCode;
import com.seeds.uc.exceptions.LoginException;
import com.seeds.uc.exceptions.RegistrationException;
import com.seeds.uc.mapper.UserMapper;
import com.seeds.uc.model.User;
import com.seeds.uc.service.CacheService;
import com.seeds.uc.service.UserLanguageService;
import com.seeds.uc.service.UserService;
import com.seeds.uc.util.PasswordUtil;
import com.seeds.uc.util.RandomUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author allen
 * @email allen.hua.ai@gmail.com
 * @date 2020/7/26
 */
@Slf4j
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private CacheService cacheService;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserDtoMapper userDtoMapper;

    @Autowired
    private UserLanguageService userLanguageService;

    @Override
    @Transactional
    public UserDto register(ClientAuthTypeEnum authTypeEnum, RegisterReq registerReq) {
        // 用户使用的phone 还是email 作为account name
        String accountName = null;

        // 查一下数据库看是不是已经有了
        int existingCount = 0;
        if (ClientAuthTypeEnum.PHONE.equals(authTypeEnum)) {
            accountName = registerReq.getPhone();
            existingCount = userMapper.countByPhone(registerReq.getPhone());
        }
        if (ClientAuthTypeEnum.EMAIL.equals(authTypeEnum)) {
            accountName = registerReq.getEmail();
            existingCount = userMapper.countByEmail(registerReq.getEmail());
        }

        // 已经有了，throw exception
        if (existingCount != 0) {
            UcErrorCode existingError =
                    ClientAuthTypeEnum.PHONE.equals(authTypeEnum) ?
                            UcErrorCode.ERR_10051_PHONE_ALREADY_BEEN_USED : UcErrorCode.ERR_10061_EMAIL_ALREADY_BEEN_USED;
            throw new RegistrationException(existingError);
        }

        String salt = RandomUtil.getRandomSalt();
        // 现在是password sha256之后的值，后面改成前端传密文且用非对称加密传
        String password = PasswordUtil.getPassword(registerReq.getPassword(), salt);
        long createTime = System.currentTimeMillis();

        User registeringUser =
                User.builder()
                        .password(password)
                        .state(ClientStateEnum.NORMAL)
                        .type(ClientTypeEnum.NORMAL)
                        .countryCode(registerReq.getCountryCode())
                        .nationality(registerReq.getNationality())
                        .phone(ClientAuthTypeEnum.PHONE.equals(authTypeEnum) ? accountName : "")
                        .email(ClientAuthTypeEnum.EMAIL.equals(authTypeEnum) ? accountName : "")
                        .gaSecret("")
                        .salt(salt)
                        .createdAt(createTime)
                        .updatedAt(createTime)
                        .build();
        userMapper.insert(registeringUser);
        return userDtoMapper.userToDto(registeringUser);
    }

    @Override
    public UserDto getUserByUcToken(String token) {
        LoginUser loginUser = cacheService.getUserByToken(token);
        return getUserByUid(loginUser.getUserId());
    }

    @Override
    public UserDto getUserByUid(Long uid) {
        User user = userMapper.selectByPrimaryKey(uid);
        return userDtoMapper.userToDto(user);
    }


    @Override
    public UserDto verifyLogin(LoginReq loginReq) {
        User user = null;
        User userByPhone = userMapper.selectByPhone(loginReq.getLoginName());
        User userByEmail = userMapper.selectByEmail(loginReq.getLoginName());
        ClientAuthTypeEnum loginType = null;
        if (userByPhone != null) {
            user = userByPhone;
            loginType = ClientAuthTypeEnum.PHONE;
        }
        if (userByEmail != null) {
            user = userByEmail;
            loginType = ClientAuthTypeEnum.EMAIL;
        }
        if (user == null) {
            throw new LoginException(UcErrorCode.ERR_10013_ACCOUNT_NAME_PASSWORD_INCORRECT);
        }
        if (ClientStateEnum.FROZEN.equals(user.getState())) {
            throw new LoginException(UcErrorCode.ERR_10014_ACCOUNT_FROZEN);
        }

        String loginPassword = PasswordUtil.getPassword(loginReq.getPassword(), user.getSalt());
        if (!loginPassword.equals(user.getPassword())) {
            throw new LoginException(UcErrorCode.ERR_10013_ACCOUNT_NAME_PASSWORD_INCORRECT);
        }
        UserDto userDto = userDtoMapper.userToDto(user);
        userDto.setAuthType(loginType);
        return userDto;
    }

    @Override
    public void logout(String token) {

    }

    @Override
    public boolean verifyPasswordByUid(Long uid, String rawPassword) {
        User user = userMapper.selectByPrimaryKey(uid);
        return user.getPassword().equals(PasswordUtil.getPassword(rawPassword, user.getSalt()));
    }

    @Override
    @Transactional
    public void setPasswordByUid(Long uid, String newPassword) {
        String salt = RandomUtil.getRandomSalt();
        // 现在是password sha256之后的值，后面改成前端传密文且用非对称加密传
        String password = PasswordUtil.getPassword(newPassword, salt);
        User user = userMapper.selectByPrimaryKey(uid);
        user.setSalt(salt);
        user.setPassword(password);
        userMapper.updateByPrimaryKey(user);
    }

    @Override
    @Transactional
    public void freezeUser(Long uid) {
        User user = userMapper.selectByPrimaryKey(uid);
        // 首先把这个用户登陆态清掉
        cacheService.removeUserByUid(uid);

        // 状态置为冻结
        user.setState(ClientStateEnum.FROZEN);
        userMapper.updateByPrimaryKey(user);
    }

    @Override
    public InterUserInfo getInternalUserInfo(Long uid){
        User user = userMapper.selectByPrimaryKey(uid);
        InterUserInfo interUserInfo = userDtoMapper.userToInterUserInfo(user);
        interUserInfo.setLanguage(userLanguageService.getUserLanguageByUid(uid));
        return interUserInfo;
    }
}
