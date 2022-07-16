package com.seeds.uc.web.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.seeds.uc.model.cache.dto.LoginUser;
import com.seeds.uc.model.user.entity.UcSecurityStrategy;
import com.seeds.uc.model.user.entity.UcUser;
import com.seeds.uc.model.user.enums.ClientAuthTypeEnum;
import com.seeds.uc.util.WebUtil;
import com.seeds.uc.web.cache.service.CacheService;
import com.seeds.uc.web.user.mapper.UcUserMapper;
import com.seeds.uc.web.user.service.IGoogleAuthService;
import com.seeds.uc.web.user.service.IUcSecurityStrategyService;
import com.warrenstrange.googleauth.GoogleAuthenticator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;

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
    @Autowired
    private CacheService cacheService;
    @Autowired
    private IUcSecurityStrategyService iUcSecurityStrategyService;

    @Override
    public String getQRBarcode(String account, String remark, HttpServletRequest request) {
        String format = "otpauth://totp/%s?secret=%s&issuer=%s";
        String gaSecret = this.genGaSecret();
        // 将gaSecret保存到数据库中
        String loginToken = WebUtil.getTokenFromRequest(request);
        LoginUser loginUser = cacheService.getUserByToken(loginToken);
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
    public boolean verifyUserCode(Long uid, String userInputCode) {
        long createTime = System.currentTimeMillis();
        UcUser user = userMapper.selectById(uid);
        boolean verify = verify(userInputCode, user.getGaSecret());
        if (verify) {
            iUcSecurityStrategyService.saveOrUpdate(UcSecurityStrategy.builder()
                    .uid(uid)
                    .needAuth(true)
                    .authType(Integer.valueOf(ClientAuthTypeEnum.GA.getCode()))
                    .createdAt(createTime)
                    .updatedAt(createTime)
                    .build(), new QueryWrapper<UcSecurityStrategy>().lambda().eq(UcSecurityStrategy::getUid, uid));
        }

        return verify;
    }

    @Override
    public boolean verify(String userInputCode, String userSecret) {
        GoogleAuthenticator googleAuthenticator = new GoogleAuthenticator();
        return googleAuthenticator.authorize(userSecret, Integer.parseInt(userInputCode.replaceFirst("0*", "")));
    }
}