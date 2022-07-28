package com.seeds.uc.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.seeds.uc.dto.redis.LoginUserDTO;
import com.seeds.uc.enums.ClientAuthTypeEnum;
import com.seeds.uc.enums.UcErrorCodeEnum;
import com.seeds.uc.exceptions.InvalidArgumentsException;
import com.seeds.uc.mapper.UcUserMapper;
import com.seeds.uc.model.UcSecurityStrategy;
import com.seeds.uc.model.UcUser;
import com.seeds.uc.service.IGoogleAuthService;
import com.seeds.uc.service.IUcSecurityStrategyService;
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

    public static String format = "otpauth://totp/%s?secret=%s&issuer=%s";
    @Autowired
    UcUserMapper userMapper;
    @Autowired
    private CacheService cacheService;
    @Autowired
    private IUcSecurityStrategyService iUcSecurityStrategyService;

    @Override
    public String getQRBarcode(String account, String remark, LoginUserDTO loginUser) {
        String gaSecret = this.genGaSecret();
        // 将gaSecret保存到数据库中
        userMapper.updateById(UcUser.builder()
                .gaSecret(gaSecret)
                .id(loginUser.getUserId())
                .build());
        return String.format(format, account, gaSecret, remark);
    }

    @Override
    public String genGaSecret() {
        GoogleAuthenticator ga = new GoogleAuthenticator();
        return ga.createCredentials().getKey();
    }

    @Override
    public Boolean verifyUserCode(Long uid, String userInputCode) {
        long createTime = System.currentTimeMillis();
        UcUser user = userMapper.selectById(uid);
        boolean verify = verify(userInputCode, user.getGaSecret());
        if (!verify) {
            throw new InvalidArgumentsException(UcErrorCodeEnum.ERR_14000_ACCOUNT_NOT);
        }
        iUcSecurityStrategyService.saveOrUpdate(UcSecurityStrategy.builder()
                .uid(uid)
                .needAuth(true)
                .authType(ClientAuthTypeEnum.GA)
                .createdAt(createTime)
                .updatedAt(createTime)
                .build(), new QueryWrapper<UcSecurityStrategy>().lambda().eq(UcSecurityStrategy::getUid, uid));
        return true;
    }

    @Override
    public boolean verify(String userInputCode, String userSecret) {
        GoogleAuthenticator googleAuthenticator = new GoogleAuthenticator();
        return googleAuthenticator.authorize(userSecret, Integer.parseInt(userInputCode.replaceFirst("0*", "")));
    }
}