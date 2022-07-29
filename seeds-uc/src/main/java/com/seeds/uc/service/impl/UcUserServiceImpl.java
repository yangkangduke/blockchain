package com.seeds.uc.service.impl;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.mail.MailAccount;
import cn.hutool.extra.mail.MailUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seeds.common.web.config.EmailProperties;
import com.seeds.uc.config.ResetPasswordProperties;
import com.seeds.uc.constant.UcConstant;
import com.seeds.uc.dto.redis.AuthCodeDTO;
import com.seeds.uc.dto.redis.ForgotPasswordCodeDTO;
import com.seeds.uc.dto.redis.LoginUserDTO;
import com.seeds.uc.dto.redis.TwoFactorAuth;
import com.seeds.uc.dto.request.*;
import com.seeds.uc.dto.response.LoginResp;
import com.seeds.uc.enums.*;
import com.seeds.uc.exceptions.InvalidArgumentsException;
import com.seeds.uc.exceptions.LoginException;
import com.seeds.uc.mapper.UcUserMapper;
import com.seeds.uc.model.UcSecurityStrategy;
import com.seeds.uc.model.UcUser;
import com.seeds.uc.service.IGoogleAuthService;
import com.seeds.uc.service.IUcSecurityStrategyService;
import com.seeds.uc.service.IUcUserService;
import com.seeds.uc.util.CryptoUtils;
import com.seeds.uc.util.DigestUtil;
import com.seeds.uc.util.PasswordUtil;
import com.seeds.uc.util.RandomUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.web3j.crypto.WalletUtils;

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
    private ResetPasswordProperties resetPasswordProperties;
    @Autowired
    private IUcSecurityStrategyService ucSecurityStrategyService;
    @Autowired
    private EmailProperties emailProperties;
    @Autowired
    private IGoogleAuthService googleAuthService;

    private MailAccount createMailAccount() {
        MailAccount account = new MailAccount();
        account.setHost(emailProperties.getHost());
        account.setPort(25);
        account.setAuth(true);
        account.setFrom(emailProperties.getFrom());
        account.setUser(emailProperties.getUser());
        account.setPass(emailProperties.getPass());
        return account;
    }


    /**
     * 注册邮箱账号
     *
     * @param registerReq
     * @return
     */
    @Override
    public LoginResp registerEmailAccount(RegisterReq registerReq) {
        String email = registerReq.getEmail();
        this.registerEmailAccountCheck(registerReq);
        String salt = RandomUtil.getRandomSalt();
        String password = PasswordUtil.getPassword(registerReq.getPassword(), salt);
        long currentTime = System.currentTimeMillis();
        UcUser ucUser = UcUser.builder()
                .email(email)
                .password(password)
                .state(ClientStateEnum.NORMAL)
                .type(ClientTypeEnum.NORMAL)
                .updatedAt(currentTime)
                .createdAt(currentTime)
                .salt(salt)
                .nickname(email)
                .build();
        this.save(ucUser);
        Long id = ucUser.getId();

        ucSecurityStrategyService.save(UcSecurityStrategy.builder()
                .uid(id)
                .needAuth(true)
                .authType(ClientAuthTypeEnum.EMAIL)
                .createdAt(currentTime)
                .updatedAt(currentTime)
                .build());

        // 注册完成，生成uc token给用户
        String ucToken = RandomUtil.genRandomToken(id.toString());
        // 将token存入redis，用户进入登陆态
        cacheService.putUserWithTokenAndLoginName(ucToken, id, email);

        return LoginResp.builder()
                .ucToken(ucToken)
                .type(ClientAuthTypeEnum.EMAIL)
                .account(email)
                .build();
    }

    private void registerEmailAccountCheck(RegisterReq registerReq) {
        // 校验code
        String code = registerReq.getCode();
        String email = registerReq.getEmail();
        AuthCodeDTO authCode = cacheService.getAuthCode(email, AuthCodeUseTypeEnum.REGISTER, ClientAuthTypeEnum.EMAIL);
        if (authCode == null) {
            throw new InvalidArgumentsException(UcErrorCodeEnum.ERR_10036_AUTH_CODE_EXPIRED);
        }
        if (!authCode.getCode().equals(code)) {
            throw new InvalidArgumentsException(UcErrorCodeEnum.ERR_10033_WRONG_EMAIL_CODE);
        }
        // 校验账号重复
        UcUser one = this.getOne(new QueryWrapper<UcUser>().lambda().eq(UcUser::getEmail, email));
        if (one != null) {
            throw new InvalidArgumentsException(UcErrorCodeEnum.ERR_10061_EMAIL_ALREADY_BEEN_USED);
        }
    }

    /**
     * 账号登陆
     *
     * @return
     */
    @Override
    public LoginResp login(LoginReq loginReq) {
        // 校验账号、密码
        String account = loginReq.getEmail();
        String password = loginReq.getPassword();
        ClientAuthTypeEnum authType = loginReq.getAuthType();
        UcUser ucUser = this.getOne(new QueryWrapper<UcUser>().lambda()
                .eq(UcUser::getEmail, account));

        if (ucUser == null) {
            throw new InvalidArgumentsException(UcErrorCodeEnum.ERR_10001_ACCOUNT_YET_NOT_REGISTERED);
        }
        String loginPassword = PasswordUtil.getPassword(password, ucUser.getSalt());
        if (!loginPassword.equals(ucUser.getPassword())) {
            throw new InvalidArgumentsException(UcErrorCodeEnum.ERR_10013_ACCOUNT_NAME_PASSWORD_INCORRECT);
        }
        // 产生2fa验证的token，用户进入2FA登陆阶段，前端再次call 2FA登陆接口需要带上2FA token
        String token = RandomUtil.genRandomToken(ucUser.getId().toString());
        if (authType.equals(ClientAuthTypeEnum.EMAIL)){
            // 发送邮件
            // generate a random code
            String otp = RandomUtil.getRandom6DigitsOTP();
            MailUtil.send(this.createMailAccount(), CollUtil.newArrayList(account), UcConstant.LOGIN_EMAIL_VERIFY_SUBJECT, UcConstant.LOGIN_EMAIL_VERIFY_CONTENT + otp, false);
            // store the auth code in auth code bucket
            cacheService.putAuthCode(
                    ucUser.getEmail(),
                    null,
                    ClientAuthTypeEnum.EMAIL,
                    otp,
                    AuthCodeUseTypeEnum.LOGIN);
            // 将2FA token存入redis，用户进入等待2FA验证态
            cacheService.put2FAInfoWithTokenAndUserAndAuthType(
                    token,
                    ucUser.getId(),
                    ucUser.getEmail(),
                    ClientAuthTypeEnum.EMAIL);
            return LoginResp.builder()
                    .token(token)
                    .type(ClientAuthTypeEnum.EMAIL)
                    .build();
        } else if (authType.equals(ClientAuthTypeEnum.GA)){
            //检查用户现在有没有GA
            UcSecurityStrategy ucSecurityStrategy = ucSecurityStrategyService.getOne(new QueryWrapper<UcSecurityStrategy>().lambda()
                    .eq(UcSecurityStrategy::getUid, ucUser.getId())
                    .eq(UcSecurityStrategy::getNeedAuth, true)
                    .eq(UcSecurityStrategy::getAuthType, ClientAuthTypeEnum.GA));

            // 存在就使用ga
            if (ucSecurityStrategy != null ) {
                // 将2FA token存入redis，用户进入等待2FA验证态
                cacheService.put2FAInfoWithTokenAndUserAndAuthType(
                        token,
                        ucUser.getId(),
                        ucUser.getEmail(),
                        ClientAuthTypeEnum.GA);
                return LoginResp.builder()
                        .token(token)
                        .type(ClientAuthTypeEnum.GA)
                        .build();
            } else {
                throw new InvalidArgumentsException(UcErrorCodeEnum.ERR_10005_SECURITY_STRATEGY_NOT_SET);
            }
        } else {
            throw new InvalidArgumentsException(UcErrorCodeEnum.ERR_504_MISSING_ARGUMENTS);
        }

    }


    /**
     * metamask验证
     *
     * @return
     */
    @Override
    public LoginResp metamaskVerify(MetaMaskReq metaMaskReq) {
        String publicAddress = metaMaskReq.getPublicAddress();
        String message = metaMaskReq.getMessage();
        String signature = metaMaskReq.getSignature();
        UserOperateEnum operateEnum = metaMaskReq.getOperateEnum();
        long currentTime = System.currentTimeMillis();
        // 地址合法性校验
        if (!WalletUtils.isValidAddress(publicAddress)) {
            // 不合法直接返回错误
            throw new InvalidArgumentsException(UcErrorCodeEnum.ERR_10003_ADDRESS_INFO);
        }
        // 校验签名信息
        if (!CryptoUtils.validate(signature, message, publicAddress)) {
            throw new InvalidArgumentsException(UcErrorCodeEnum.ERR_10004_SIGNATURE_INFO);
        }
        UcUser one = this.getOne(new QueryWrapper<UcUser>().lambda()
                .eq(UcUser::getPublicAddress, publicAddress));

        if (operateEnum.equals(UserOperateEnum.REGISTER)) {
            ucSecurityStrategyService.save(UcSecurityStrategy.builder()
                    .needAuth(true)
                    .uid(one.getId())
                    .authType(ClientAuthTypeEnum.METAMASK)
                    .createdAt(currentTime)
                    .updatedAt(currentTime)
                    .build());
        }

        // 下发uc token
        String ucToken = RandomUtil.genRandomToken(one.getId().toString());
        // 将产生的uc token存入redis
        cacheService.putUserWithTokenAndLoginName(ucToken, one.getId(), publicAddress);
        // 修改user信息
        this.update(UcUser.builder()
                .updatedAt(currentTime)
                .nonce(RandomUtil.getRandomSalt())
                .build(), new QueryWrapper<UcUser>().lambda()
                .eq(UcUser::getPublicAddress, publicAddress));

        return LoginResp.builder().ucToken(ucToken).build();
    }

    /**
     * metamask获取随机数
     *
     * @return
     */
    @Override
    public String metamaskNonce(MetaMaskReq metaMaskReq, UcUser loginUserDTO) {
        long currentTime = System.currentTimeMillis();
        String publicAddress = metaMaskReq.getPublicAddress();
        String nonce = null;
        UcUser one = this.getOne(new QueryWrapper<UcUser>().lambda()
                .eq(UcUser::getPublicAddress, publicAddress));
        // 注册
        if (metaMaskReq.getOperateEnum().equals(UserOperateEnum.REGISTER)) {
            if (one != null) {
                throw new InvalidArgumentsException(UcErrorCodeEnum.ERR_10029_METAMASK_EXIST);
            }
            nonce = RandomUtil.getRandomSalt();
            UcUser ucUser = UcUser.builder()
                    .createdAt(currentTime)
                    .updatedAt(currentTime)
                    .type(ClientTypeEnum.NORMAL)
                    .state(ClientStateEnum.NORMAL)
                    .publicAddress(publicAddress)
                    .nonce(nonce)
                    .nickname(publicAddress)
                    .build();
            this.save(ucUser);
            // 登陆
        }else if (metaMaskReq.getOperateEnum().equals(UserOperateEnum.LOGIN)) {
            nonce = one.getNonce();
        } else if (metaMaskReq.getOperateEnum().equals(UserOperateEnum.BIND)) {
            if (loginUserDTO != null) {
                nonce = loginUserDTO.getNonce();
            }

        }
        return nonce;
    }

    /**
     * 忘记密码-发送邮件
     *
     * @param forgotPasswordReq
     */
    @Override
    public void forgotPasswordSeedEmail(ForgotPasswordReq forgotPasswordReq) {
        String account = forgotPasswordReq.getAccount();
        String otp = RandomUtil.getRandom6DigitsOTP();
        // 判断账号是否存在
        UcUser one = this.getOne(new QueryWrapper<UcUser>().lambda().eq(UcUser::getEmail, account));
        if (one == null) {
            throw new InvalidArgumentsException(UcErrorCodeEnum.ERR_15000_ACCOUNT_NOT);
        }
        // 发送加密邮件 10分钟过期
        long endTimes = System.currentTimeMillis() + 600 * 1000;
        // 先加密，再url转码,顺序不能修改
        String encode = Base64.encode(DigestUtil.Encrypt(account + UcConstant.FORGOT_PASSWORD_EMAIL_LINK_SYMBOL + otp));
        MailUtil.send(this.createMailAccount(), CollUtil.newArrayList(account), UcConstant.FORGOT_PASSWORD_EMAIL_SUBJECT, UcConstant.FORGOT_PASSWORD_EMAIL_CONTENT + "<a>" + resetPasswordProperties.getUrl() + encode + "</a>", true);
        cacheService.putForgotPasswordCode(account, otp, endTimes);
    }

    /**
     * 忘记密码-验证链接
     *
     * @param encode
     */
    @Override
    public void forgotPasswordVerifyLink(String encode) {
        if (StrUtil.isNotBlank(encode)) {
            try {
                String decodeStr = Base64.decodeStr(encode);
                String decrypt = DigestUtil.Decrypt(decodeStr);
                String[] split = decrypt.split(UcConstant.FORGOT_PASSWORD_EMAIL_LINK_SYMBOL);
                String account = split[0];
                String code = split[1];
                long curtime = System.currentTimeMillis();
                ForgotPasswordCodeDTO forgotPasswordCode = cacheService.getForgotPasswordCode(account);
                if (forgotPasswordCode == null) {
                    throw new InvalidArgumentsException(UcErrorCodeEnum.ERR_15001_ACCOUNT_VERIFICATION_FAILED);
                }
                if (forgotPasswordCode.getExpireAt() <= curtime) {
                    throw new InvalidArgumentsException(UcErrorCodeEnum.ERR_15001_ACCOUNT_VERIFICATION_FAILED);
                }
                if (!forgotPasswordCode.getCode().equals(code)) {
                    throw new InvalidArgumentsException(UcErrorCodeEnum.ERR_15002_LINK_EXPIRED);
                }
            } catch (Exception e) {
                throw new InvalidArgumentsException(UcErrorCodeEnum.ERR_15002_LINK_EXPIRED);
            }
        } else {
            throw new InvalidArgumentsException(UcErrorCodeEnum.ERR_15001_ACCOUNT_VERIFICATION_FAILED);
        }
    }

    /**
     * 忘记密码-修改密码
     *
     * @param changePasswordReq
     */
    @Override
    public void forgotPasswordChangePassword(ChangePasswordReq changePasswordReq) {
        String account = changePasswordReq.getAccount();
        String salt = RandomUtil.getRandomSalt();
        String password = PasswordUtil.getPassword(changePasswordReq.getPassword(), salt);
        this.update(UcUser.builder().password(password).salt(salt).build(), new QueryWrapper<UcUser>().lambda().eq(UcUser::getEmail, account));
    }

    /**
     * 注册邮箱账号-发送邮箱验证码
     *
     * @param email
     */
    @Override
    public void registerEmailSend(String email) {
        String otp = RandomUtil.getRandom6DigitsOTP();
        MailUtil.send(this.createMailAccount(), CollUtil.newArrayList(email), UcConstant.REGISTER_EMAIL_SUBJECT, UcConstant.REGISTER_EMAIL_CONTENT + otp, false);
        cacheService.putAuthCode(email, null, ClientAuthTypeEnum.EMAIL, otp, AuthCodeUseTypeEnum.REGISTER);

    }

    /**
     *  2fa校验
     * @param loginReq
     * @return
     */
    @Override
    public LoginResp twoFactorCheck(TwoFactorLoginReq loginReq) {
        log.info("verifyTwoFactorLogin: {}", loginReq);
        TwoFactorAuth twoFactorAuth = cacheService.get2FAInfoWithToken(loginReq.getToken());
        if (twoFactorAuth != null && twoFactorAuth.getAuthAccountName() != null) {
            // GA验证
            if (ClientAuthTypeEnum.GA.equals(twoFactorAuth.getAuthType())) {
                if (!googleAuthService.verifyUserCode(twoFactorAuth.getUserId(), loginReq.getAuthCode())) {
                    throw new LoginException(UcErrorCodeEnum.ERR_10088_WRONG_GOOGLE_AUTHENTICATOR_CODE);
                }
            } else if (ClientAuthTypeEnum.EMAIL.equals(twoFactorAuth.getAuthType())) {
                // 邮箱验证
                AuthCodeDTO authCode = cacheService.getAuthCode(
                                twoFactorAuth.getAuthAccountName(),
                                AuthCodeUseTypeEnum.LOGIN,
                                twoFactorAuth.getAuthType());
                if (authCode == null ||  authCode.getCode() == null || !authCode.getCode().equals(loginReq.getAuthCode()) ) {
                    throw new LoginException(UcErrorCodeEnum.ERR_14000_ACCOUNT_NOT);
                }

            } else {
                throw new LoginException(UcErrorCodeEnum.ERR_14000_ACCOUNT_NOT);
            }
        } else {
            throw new LoginException(UcErrorCodeEnum.ERR_14000_ACCOUNT_NOT);
        }

        UcUser user = this.getById(twoFactorAuth.getUserId());
        // 用户验证通过，产生uc token
        String ucToken = RandomUtil.genRandomToken(user.getId().toString());

        // 将产生的uc token存入redis
        cacheService.putUserWithTokenAndLoginName(ucToken, user.getId(), twoFactorAuth.getAuthAccountName());

        return  LoginResp.builder()
                        .ucToken(ucToken)
                        .build();
    }


}
