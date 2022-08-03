package com.seeds.uc.service.impl;

import cn.hutool.core.bean.BeanUtil;
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
import com.seeds.uc.dto.UserDto;
import com.seeds.uc.dto.redis.AuthCodeDTO;
import com.seeds.uc.dto.redis.LoginUserDTO;
import com.seeds.uc.dto.redis.TwoFactorAuth;
import com.seeds.uc.dto.request.*;
import com.seeds.uc.dto.response.LoginResp;
import com.seeds.uc.dto.response.ProfileResp;
import com.seeds.uc.dto.response.UcSecurityStrategyResp;
import com.seeds.uc.enums.*;
import com.seeds.uc.exceptions.InvalidArgumentsException;
import com.seeds.uc.exceptions.LoginException;
import com.seeds.uc.mapper.UcUserMapper;
import com.seeds.uc.model.UcSecurityStrategy;
import com.seeds.uc.model.UcUser;
import com.seeds.uc.service.IGoogleAuthService;
import com.seeds.uc.service.IUcSecurityStrategyService;
import com.seeds.uc.service.IUcUserService;
import com.seeds.uc.service.SendCodeService;
import com.seeds.uc.util.CryptoUtils;
import com.seeds.uc.util.DigestUtil;
import com.seeds.uc.util.PasswordUtil;
import com.seeds.uc.util.RandomUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.web3j.crypto.WalletUtils;

import java.util.List;

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
    @Autowired
    private SendCodeService sendCodeService;

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
        sendCodeService.verifyEmailWithUseType(registerReq.getEmail(),registerReq.getCode(),AuthCodeUseTypeEnum.REGISTER);
        // 校验账号重复
        UcUser one = this.getOne(new QueryWrapper<UcUser>().lambda().eq(UcUser::getEmail, email));
        if (one != null) {
            throw new InvalidArgumentsException(UcErrorCodeEnum.ERR_10061_EMAIL_ALREADY_BEEN_USED);
        }
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

    /**
     * 账号登陆
     *
     * @return
     */
    @Override
    public LoginResp login(LoginReq loginReq) {
        // 校验账号、密码
//        ClientAuthTypeEnum authType = loginReq.getAuthType();
//        if (authType == null) {
//            throw new InvalidArgumentsException("Please enter authType");
//        }
        UserDto userDto = this.verifyLogin(loginReq);
        // 产生2fa验证的token，用户进入2FA登陆阶段，前端再次call 2FA登陆接口需要带上2FA token
        String token = RandomUtil.genRandomToken(userDto.getUid().toString());
        //检查用户现在有没有GA
        UcSecurityStrategy ucSecurityStrategy = ucSecurityStrategyService.getOne(new QueryWrapper<UcSecurityStrategy>().lambda()
                .eq(UcSecurityStrategy::getUid, userDto.getUid())
                .eq(UcSecurityStrategy::getNeedAuth, true)
                .eq(UcSecurityStrategy::getAuthType, ClientAuthTypeEnum.GA));

        // 存在就使用ga
        if (ucSecurityStrategy != null ) {
            // 将2FA token存入redis，用户进入等待2FA验证态
            cacheService.put2FAInfoWithTokenAndUserAndAuthType(
                    token,
                    userDto.getUid(),
                    userDto.getEmail(),
                    ClientAuthTypeEnum.GA);
            return LoginResp.builder()
                    .token(token)
                    .type(ClientAuthTypeEnum.GA)
                    .build();
        } else {
            // 发送邮件
            // generate a random code
            sendCodeService.sendUserCodeByUseType(userDto, AuthCodeUseTypeEnum.LOGIN);
            // store the auth code in auth code bucket
            cacheService.put2FAInfoWithTokenAndUserAndAuthType(
                    token,
                    userDto.getUid(),
                    userDto.getEmail(),
                    ClientAuthTypeEnum.EMAIL);

            // 将2FA token存入redis，用户进入等待2FA验证态
            cacheService.put2FAInfoWithTokenAndUserAndAuthType(
                    token,
                    userDto.getUid(),
                    userDto.getEmail(),
                    ClientAuthTypeEnum.EMAIL);

            return LoginResp.builder()
                    .token(token)
                    .type(ClientAuthTypeEnum.EMAIL)
                    .account(userDto.getEmail())
                    .build();
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
        try{
            if (!CryptoUtils.validate(signature, message, publicAddress)) {
                throw new InvalidArgumentsException(UcErrorCodeEnum.ERR_10004_SIGNATURE_INFO);
            }
        } catch (Exception e) {
            throw new InvalidArgumentsException(UcErrorCodeEnum.ERR_10004_SIGNATURE_INFO);
        }

        UcUser one = this.getOne(new QueryWrapper<UcUser>().lambda()
                .eq(UcUser::getPublicAddress, publicAddress));
        if (one == null) {
            throw new InvalidArgumentsException(UcErrorCodeEnum.ERR_10004_SIGNATURE_INFO);
        }
        if (operateEnum.equals(UserOperateEnum.REGISTER) || operateEnum.equals(UserOperateEnum.BIND) ) {
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

        // 登陆/注册
        if (metaMaskReq.getOperateEnum().equals(UserOperateEnum.REGISTER) || metaMaskReq.getOperateEnum().equals(UserOperateEnum.LOGIN)) {
            if (loginUserDTO == null) {
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
            } else {
                nonce = loginUserDTO.getNonce();
            }

            // 绑定
        } else if (metaMaskReq.getOperateEnum().equals(UserOperateEnum.BIND)) {
            if (loginUserDTO == null) {
                throw new InvalidArgumentsException("Please login");
            }
            // 修改用户信息
            nonce = RandomUtil.getRandomSalt();
            UcUser ucUser = UcUser.builder()
                    .updatedAt(currentTime)
                    .publicAddress(publicAddress)
                    .nonce(nonce)
                    .id(loginUserDTO.getId())
                    .build();
            this.updateById(ucUser);
        }
        return nonce;
    }

    /**
     * 忘记密码-验证链接
     *
     * @param encode
     */
    @Override
    public void forgotPasswordVerifyLink(String encode, String account) {
        if (StrUtil.isNotBlank(encode)) {
            try {
                String decodeStr = Base64.decodeStr(encode);
                String decrypt = DigestUtil.Decrypt(decodeStr);
                String[] split = decrypt.split(";");
                String code = split[1];
                long curtime = System.currentTimeMillis();
                AuthCodeDTO authCode = cacheService.getAuthCode(account, AuthCodeUseTypeEnum.RESET_PASSWORD, ClientAuthTypeEnum.EMAIL);
                if (authCode == null) {
                    throw new InvalidArgumentsException(UcErrorCodeEnum.ERR_15001_ACCOUNT_VERIFICATION_FAILED);
                }
                if (authCode.getExpireAt() <= curtime) {
                    throw new InvalidArgumentsException(UcErrorCodeEnum.ERR_15001_ACCOUNT_VERIFICATION_FAILED);
                }
                if (!authCode.getCode().equals(code)) {
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
    public void forgotPasswordReset(ChangePasswordReq changePasswordReq) {
        String account = changePasswordReq.getAccount();
        String salt = RandomUtil.getRandomSalt();
        String password = PasswordUtil.getPassword(changePasswordReq.getPassword(), salt);
        this.update(UcUser.builder().password(password).salt(salt).build(),
                new QueryWrapper<UcUser>().lambda().eq(UcUser::getEmail, account));
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

                log.info("verifyTwoFactorLogin - in redis twoFactorAuth:{} and authCode: {}", twoFactorAuth, authCode);
                if (authCode == null) {
                    throw new LoginException(UcErrorCodeEnum.ERR_10036_AUTH_CODE_EXPIRED);
                }
                if (!authCode.getCode().equals(loginReq.getAuthCode())) {
                    throw new LoginException(UcErrorCodeEnum.ERR_10033_WRONG_EMAIL_CODE);
                }

            } else {
                throw new LoginException(UcErrorCodeEnum.ERR_504_MISSING_ARGUMENTS);
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

    /**
     * 校验登陆
     * @param loginReq
     * @return
     */
    @Override
    public UserDto verifyLogin(LoginReq loginReq) {
        // 校验账号、密码
        String account = loginReq.getEmail();
        String password = loginReq.getPassword();
        UcUser ucUser = this.getOne(new QueryWrapper<UcUser>().lambda()
                .eq(UcUser::getEmail, account));

        if (ucUser == null) {
            throw new InvalidArgumentsException(UcErrorCodeEnum.ERR_10001_ACCOUNT_YET_NOT_REGISTERED);
        }
        String loginPassword = PasswordUtil.getPassword(password, ucUser.getSalt());
        if (!loginPassword.equals(ucUser.getPassword())) {
            throw new InvalidArgumentsException(UcErrorCodeEnum.ERR_10013_ACCOUNT_NAME_PASSWORD_INCORRECT);
        }
        UserDto userDto = new UserDto();
        BeanUtil.copyProperties(ucUser, userDto);
        userDto.setAuthType(ClientAuthTypeEnum.EMAIL);
        userDto.setUid(ucUser.getId());
        return userDto;
    }

    /**
     * 获取用户信息
     * @param loginUser
     * @return
     */
    @Override
    public ProfileResp getMyProfile(LoginUserDTO loginUser) {
        ProfileResp profileResp = ProfileResp.builder().build();
        Long userId = loginUser.getUserId();
        BeanUtil.copyProperties(this.getById(userId), profileResp);
        profileResp.setSecurityStrategyList(ucSecurityStrategyService.getByUserId(userId));
        return profileResp;
    }


}
