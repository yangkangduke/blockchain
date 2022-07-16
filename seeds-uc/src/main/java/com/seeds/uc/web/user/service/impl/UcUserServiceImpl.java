package com.seeds.uc.web.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seeds.uc.exceptions.GenericException;
import com.seeds.uc.model.cache.dto.AuthCode;
import com.seeds.uc.model.cache.dto.LoginUser;
import com.seeds.uc.model.send.dto.request.BndEmailReq;
import com.seeds.uc.model.send.dto.request.EmailCodeSendReq;
import com.seeds.uc.model.user.dto.request.LoginReq;
import com.seeds.uc.model.user.dto.request.RegisterReq;
import com.seeds.uc.model.user.dto.response.LoginResp;
import com.seeds.uc.model.user.entity.UcSecurityStrategy;
import com.seeds.uc.model.user.entity.UcUser;
import com.seeds.uc.model.user.enums.ClientAuthTypeEnum;
import com.seeds.uc.model.user.enums.ClientStateEnum;
import com.seeds.uc.model.user.enums.ClientTypeEnum;
import com.seeds.uc.model.user.enums.UcErrorCode;
import com.seeds.uc.util.CryptoUtils;
import com.seeds.uc.util.PasswordUtil;
import com.seeds.uc.util.RandomUtil;
import com.seeds.uc.util.WebUtil;
import com.seeds.uc.web.cache.service.CacheService;
import com.seeds.uc.web.send.service.SendCodeService;
import com.seeds.uc.web.user.mapper.UcUserMapper;
import com.seeds.uc.web.user.service.IUcSecurityStrategyService;
import com.seeds.uc.web.user.service.IUcUserService;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.web3j.crypto.WalletUtils;

import javax.servlet.http.HttpServletRequest;

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
@Transactional
public class UcUserServiceImpl extends ServiceImpl<UcUserMapper, UcUser> implements IUcUserService {
    @Autowired
    private CacheService cacheService;
    @Autowired
    private SendCodeService sendCodeService;
    @Autowired
    private IUcSecurityStrategyService iUcSecurityStrategyService;
    @Autowired
    private RedissonClient redissonClient;

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
    public Boolean bindEmail(BndEmailReq verifyReq, HttpServletRequest request) {
        String email = verifyReq.getEmail();
        String code = verifyReq.getCode();
        AuthCode authCode = cacheService.getAuthCode(email, verifyReq.getUseType(), ClientAuthTypeEnum.EMAIL);
        if (authCode == null || authCode.getCode() == null || !authCode.getCode().equals(code)) {
            throw new GenericException(UcErrorCode.ERR_10033_WRONG_EMAIL_CODE);
        }
        // 获取当前登陆人信息
        String loginToken = WebUtil.getTokenFromRequest(request);
        LoginUser loginUser = cacheService.getUserByToken(loginToken);
        Long userId = loginUser.getUserId();
        // 将邮箱绑定到uc_security_strategy
        long createTime = System.currentTimeMillis();

        // 修改邮箱信息到user表
        this.updateById(UcUser.builder()
                .email(email)
                .id(userId)
                .build());

        return iUcSecurityStrategyService.saveOrUpdate(UcSecurityStrategy.builder()
                .uid(userId)
                .needAuth(true)
                .authType(Integer.valueOf(ClientAuthTypeEnum.EMAIL.getCode()))
                .createdAt(createTime)
                .updatedAt(createTime)
                .build(), new QueryWrapper<UcSecurityStrategy>().lambda().eq(UcSecurityStrategy::getUid, userId));
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
    public LoginResp createAccount(RegisterReq registerReq, HttpServletRequest request) {
        LoginUser loginUser = null;
        // 获取当前登陆人信息
        String loginToken = WebUtil.getTokenFromRequest(request);
        if (loginToken != null) {
            loginUser = cacheService.getUserByToken(loginToken);
        }
        // loginUser不是null说明当前登陆的是metamsk,还要绑定metamsk
        String account = registerReq.getAccount();
        this.accountVerify(account);
        String salt = RandomUtil.getRandomSalt();
        // 现在是password sha256之后的值，后面改成前端传密文且用非对称加密传
        String password = PasswordUtil.getPassword(registerReq.getPassword(), salt);
        long createTime = System.currentTimeMillis();
        UcUser ucUser;
        if (loginUser == null) {
            ucUser = UcUser.builder()
                    .account(account)
                    .password(password)
                    .createdAt(createTime)
                    .updatedAt(createTime)
                    .salt(salt)
                    .type(ClientTypeEnum.NORMAL)
                    .state(ClientStateEnum.NORMAL).build();

            this.save(ucUser);
        } else {
            ucUser = UcUser.builder()
                    .id(loginUser.getUserId())
                    .account(account)
                    .password(password)
                    .updatedAt(createTime)
                    .salt(salt).build();

            this.saveOrUpdate(ucUser,new QueryWrapper<UcUser>().lambda()
                    .eq(UcUser::getId,loginUser.getUserId()));
        }

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
     *
     * @param loginReq
     * @return
     */
    @Override
    public LoginResp loginToEmailAccount(LoginReq loginReq) {
        String account = loginReq.getAccount();
        UcUser ucUser = this.getOne(new QueryWrapper<UcUser>().lambda()
                .eq(UcUser::getAccount, account));
        if (ucUser == null) {
            throw new GenericException("Account does not exist");
        }
        String loginPassword = PasswordUtil.getPassword(loginReq.getPassword(), ucUser.getSalt());
        if (!loginPassword.equals(ucUser.getPassword())) {
            throw new GenericException(UcErrorCode.ERR_10013_ACCOUNT_NAME_PASSWORD_INCORRECT);
        }
        // 下发uc token
        String ucToken = RandomUtil.genRandomToken(ucUser.getId().toString());
        // 将产生的uc token存入redis
        cacheService.putUserWithTokenAndLoginName(ucToken, ucUser.getId(), account);

        return LoginResp.builder()
                .ucToken(ucToken)
                .build();
    }

    /**
     * metamask登陆
     *
     * @param publicAddress
     * @param signature
     * @param message
     * @param request
     * @return
     */
    @Override
    public LoginResp loginToMetamask(String publicAddress, String signature, String message, HttpServletRequest request) {
        // 地址合法性校验
        if (!WalletUtils.isValidAddress(publicAddress)) {
            // 不合法直接返回错误
            throw new GenericException("Illegal address format");
        }
        // 校验签名信息
        if (!CryptoUtils.validate(signature, message, publicAddress)) {
            throw new GenericException("Signature verification failed");
        }
        UcUser one = this.getOne(new QueryWrapper<UcUser>().lambda()
                .eq(UcUser::getSalt, publicAddress));
        if (one == null) {
            throw new GenericException("User does not exist");
        }
        // 下发uc token
        String ucToken = RandomUtil.genRandomToken(one.getId().toString());
        // 将产生的uc token存入redis
        cacheService.putUserWithTokenAndLoginName(ucToken, one.getId(), publicAddress);

        // 修改nonce
        this.loginToMetamaskNonce(publicAddress, request);

        return LoginResp.builder()
                .ucToken(ucToken)
                .build();
    }

    /**
     * metamask登陆获取随机数
     *
     * @return
     */
    @Override
    public String loginToMetamaskNonce(String publicAddress, HttpServletRequest request) {
        LoginUser loginUser = null;
        // 获取当前登陆人信息
        String loginToken = WebUtil.getTokenFromRequest(request);
        if (loginToken != null) {
            loginUser = cacheService.getUserByToken(loginToken);
        }
        long createTime = System.currentTimeMillis();
        String nonce = RandomUtil.getRandomSalt();

        if (loginUser == null) {
            // 新建一个
            UcUser ucUser = UcUser.builder()
                    .createdAt(createTime)
                    .updatedAt(createTime)
                    .type(ClientTypeEnum.NORMAL)
                    .state(ClientStateEnum.NORMAL)
                    .publicAddress(publicAddress)
                    .nonce(nonce)
                    .build();
            this.save(ucUser);
        } else {
            // 修改nonce
            UcUser ucUser = UcUser.builder()
                    .updatedAt(createTime)
                    .nonce(nonce)
                    .publicAddress(publicAddress)
                    .build();
            this.update(ucUser, new QueryWrapper<UcUser>().lambda()
                    .eq(UcUser::getId, loginUser.getUserId()));
        }
        return nonce;
    }



}
