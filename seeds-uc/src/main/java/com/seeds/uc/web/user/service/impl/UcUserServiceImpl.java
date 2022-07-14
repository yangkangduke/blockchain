package com.seeds.uc.web.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seeds.uc.exceptions.GenericException;
import com.seeds.uc.model.cache.dto.AuthCode;
import com.seeds.uc.model.send.dto.request.BndEmailReq;
import com.seeds.uc.model.send.dto.request.EmailCodeSendReq;
import com.seeds.uc.model.user.dto.request.LoginReq;
import com.seeds.uc.model.user.dto.request.RegisterReq;
import com.seeds.uc.model.user.dto.response.LoginResp;
import com.seeds.uc.model.user.entity.UcUser;
import com.seeds.uc.model.user.enums.ClientAuthTypeEnum;
import com.seeds.uc.model.user.enums.ClientStateEnum;
import com.seeds.uc.model.user.enums.ClientTypeEnum;
import com.seeds.uc.model.user.enums.UcErrorCode;
import com.seeds.uc.util.PasswordUtil;
import com.seeds.uc.util.RandomUtil;
import com.seeds.uc.web.cache.service.CacheService;
import com.seeds.uc.web.send.service.SendCodeService;
import com.seeds.uc.web.user.mapper.UcUserMapper;
import com.seeds.uc.web.user.service.IUcUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * user table 服务实现类
 * </p>
 *
 * @author yk
 * @since 2022-07-09
 */
@Service
@Slf4j
public class UcUserServiceImpl extends ServiceImpl<UcUserMapper, UcUser> implements IUcUserService {
    @Autowired
    private CacheService cacheService;
    @Autowired
    private SendCodeService sendCodeService;

    /**
     * 发送邮箱验证码
     *
     * @param sendReq
     */
    @Override
    public Boolean sendEmailCode(EmailCodeSendReq sendReq) {
        // todo 需要有登陆后的uctoken才能使用
        log.info("AuthController - sendEmailCode got request: {}", sendReq);
        sendCodeService.sendEmailWithUseType(sendReq.getEmail(), sendReq.getUseType());

        return true;
    }

    /**
     * 邮箱验证码校验
     *
     * @param verifyReq
     */
    @Override
    public Boolean bindEmail(BndEmailReq verifyReq) {
        String email = verifyReq.getEmail();
        String code = verifyReq.getCode();
        AuthCode authCode = cacheService.getAuthCode(email, verifyReq.getUseType(), ClientAuthTypeEnum.EMAIL);
        if (authCode == null || authCode.getCode() == null || !authCode.getCode().equals(code)) {
            throw new GenericException(UcErrorCode.ERR_10033_WRONG_EMAIL_CODE);
        }
        // todo 需要添加数据到数据库
        return true;
    }

    /**
     * 账号重复性校验
     *
     * @param account
     * @return
     */
    @Override
    public Boolean accountVerify(String account) {
        UcUser ucUser = this.getOne(new QueryWrapper<UcUser>().lambda()
                .eq(UcUser::getAccount, account));
        if (ucUser != null) {
            throw new GenericException(UcErrorCode.ERR_10061_EMAIL_ALREADY_BEEN_USED);
        }
        return true;
    }

    /**
     * 注册用户
     *
     * @param registerReq
     * @return
     */
    @Override
    public LoginResp createAccount(RegisterReq registerReq) {
        String account = registerReq.getAccount();
        this.accountVerify(account);
        String salt = RandomUtil.getRandomSalt();
        // 现在是password sha256之后的值，后面改成前端传密文且用非对称加密传
        String password = PasswordUtil.getPassword(registerReq.getPassword(), salt);
        long createTime = System.currentTimeMillis();
        UcUser ucUser = UcUser.builder()
                .account(account)
                .password(password)
                .createdAt(createTime)
                .updatedAt(createTime)
                .salt(salt)
                .type(ClientTypeEnum.NORMAL)
                .state(ClientStateEnum.NORMAL)
                .build();
        this.save(ucUser);
        Long id = ucUser.getId();

        // 注册完成，生成uc token给用户
        String ucToken = RandomUtil.genRandomToken(id.toString());
        // 将token存入redis，用户进入登陆态
        cacheService.putUserWithTokenAndLoginName(ucToken, id, account);
        LoginResp loginResp = LoginResp.builder()
                .ucToken(ucToken)
                .build();

        return loginResp;
    }

    /**
     * 登陆
     * @param loginReq
     * @return
     */
    @Override
    public LoginResp login(LoginReq loginReq) {
        String account = loginReq.getAccount();
        UcUser ucUser = this.getOne(new QueryWrapper<UcUser>().lambda()
                .eq(UcUser::getAccount, account));
        if (ucUser == null) {
            throw new GenericException("账号不存在");
        }
        String loginPassword = PasswordUtil.getPassword(loginReq.getPassword(), ucUser.getSalt());
        if (!loginPassword.equals(ucUser.getPassword())) {
            throw new GenericException(UcErrorCode.ERR_10013_ACCOUNT_NAME_PASSWORD_INCORRECT);
        }
        // 不需要2FA验证，直接下发uc token
        String ucToken = RandomUtil.genRandomToken(ucUser.getId().toString());
        // 将产生的uc token存入redis
        cacheService.putUserWithTokenAndLoginName(ucToken, ucUser.getId(), account);

        return LoginResp.builder()
                .ucToken(ucToken)
                .build();
    }
}
