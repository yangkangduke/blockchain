package com.seeds.uc.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seeds.uc.dto.UserDto;
import com.seeds.uc.dto.redis.AuthCodeDTO;
import com.seeds.uc.dto.redis.GenMetamaskAuth;
import com.seeds.uc.dto.redis.LoginUserDTO;
import com.seeds.uc.dto.redis.TwoFactorAuth;
import com.seeds.uc.dto.request.*;
import com.seeds.uc.dto.response.LoginResp;
import com.seeds.uc.dto.response.UserInfoResp;
import com.seeds.uc.enums.*;
import com.seeds.uc.exceptions.InvalidArgumentsException;
import com.seeds.uc.exceptions.LoginException;
import com.seeds.uc.mapper.UcSecurityStrategyMapper;
import com.seeds.uc.mapper.UcUserMapper;
import com.seeds.uc.model.UcSecurityStrategy;
import com.seeds.uc.model.UcUser;
import com.seeds.uc.service.IGoogleAuthService;
import com.seeds.uc.service.IUcUserService;
import com.seeds.uc.service.SendCodeService;
import com.seeds.uc.util.CryptoUtils;
import com.seeds.uc.util.DigestUtil;
import com.seeds.uc.util.PasswordUtil;
import com.seeds.uc.util.RandomUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
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
    private UcSecurityStrategyMapper ucSecurityStrategyMapper;
    @Autowired
    private IGoogleAuthService googleAuthService;
    @Autowired
    private SendCodeService sendCodeService;


    /**
     * 注册邮箱账号
     *
     * @param registerReq
     * @return
     */
    @Override
    public LoginResp registerEmailAccount(RegisterReq registerReq) {
        String email = registerReq.getEmail();
        sendCodeService.verifyEmailWithUseType(registerReq.getEmail(), registerReq.getCode(), AuthCodeUseTypeEnum.REGISTER);
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

        ucSecurityStrategyMapper.insert(UcSecurityStrategy.builder()
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
        // 检查用户现在有没有GA,存在就使用ga
        if (this.verifyGa(userDto.getUid())) {
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
     * metamask登陆
     *
     * @return
     */
    @Override
    public LoginResp metamaskLogin(MetaMaskReq metaMaskReq) {
        String publicAddress = metaMaskReq.getPublicAddress();
        String message = metaMaskReq.getMessage();
        String signature = metaMaskReq.getSignature();
        long currentTime = System.currentTimeMillis();
        GenMetamaskAuth genMetamaskAuth = cacheService.getGenerateMetamaskAuth(metaMaskReq.getPublicAddress());
//        if (genMetamaskAuth == null || StringUtils.isBlank(genMetamaskAuth.getNonce()) ) {
//            throw new InvalidArgumentsException(UcErrorCodeEnum.ERR_16003_METAMASK_NONCE_EXPIRED);
//        }
        // 地址合法性校验
        if (!WalletUtils.isValidAddress(publicAddress)) {
            // 不合法直接返回错误
            throw new InvalidArgumentsException(UcErrorCodeEnum.ERR_16001_METAMASK_ADDRESS);
        }
        // 校验签名信息
        try{
            if (!CryptoUtils.validate(signature, message, publicAddress)) {
                throw new InvalidArgumentsException(UcErrorCodeEnum.ERR_16002_METAMASK_SIGNATURE);
            }
        } catch (Exception e) {
            throw new InvalidArgumentsException(UcErrorCodeEnum.ERR_16002_METAMASK_SIGNATURE);
        }

        UcUser one = this.getOne(new QueryWrapper<UcUser>().lambda()
                .eq(UcUser::getPublicAddress, publicAddress));
        Long userId;
        if (one == null) {
            // 新增
            UcUser ucUser = UcUser.builder()
                    .nickname(publicAddress)
                    .publicAddress(publicAddress)
                    .nonce(genMetamaskAuth.getNonce())
                    .state(ClientStateEnum.NORMAL)
                    .type(ClientTypeEnum.NORMAL)
                    .createdAt(currentTime)
                    .updatedAt(currentTime)
                    .build();
            this.save(ucUser);
            userId = ucUser.getId();
        } else {
            userId = one.getId();
        }
        UcSecurityStrategy ucSecurityStrategy = ucSecurityStrategyMapper.selectOne(new QueryWrapper<UcSecurityStrategy>().lambda()
                .eq(UcSecurityStrategy::getUid, userId)
                .eq(UcSecurityStrategy::getAuthType, ClientAuthTypeEnum.METAMASK));
        if (ucSecurityStrategy == null) {
            // 新增
            ucSecurityStrategyMapper.insert(UcSecurityStrategy.builder()
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

        return LoginResp.builder().ucToken(ucToken).build();
    }


    @Override
    public void bindMetamask(MetaMaskReq metaMaskReq, Long uId) {
//        String publicAddress = metaMaskReq.getPublicAddress();
//        String nonce = metaMaskReq.getNonce();
//        String signature = metaMaskReq.getSignature();
//        long currentTime = System.currentTimeMillis();
//        GenMetamaskAuth genMetamaskAuth = cacheService.getGenerateMetamaskAuth(metaMaskReq.getPublicAddress());
//        if (genMetamaskAuth == null || StringUtils.isBlank(genMetamaskAuth.getNonce()) || !nonce.equals(genMetamaskAuth.getNonce())) {
//            throw new InvalidArgumentsException(UcErrorCodeEnum.ERR_16003_METAMASK_NONCE_EXPIRED);
//        }
//        // 地址合法性校验
//        if (!WalletUtils.isValidAddress(publicAddress)) {
//            // 不合法直接返回错误
//            throw new InvalidArgumentsException(UcErrorCodeEnum.ERR_16001_METAMASK_ADDRESS);
//        }
//        // 校验签名信息
//        try{
//            if (!CryptoUtils.validate(signature, nonce, publicAddress)) {
//                throw new InvalidArgumentsException(UcErrorCodeEnum.ERR_16002_METAMASK_SIGNATURE);
//            }
//        } catch (Exception e) {
//            throw new InvalidArgumentsException(UcErrorCodeEnum.ERR_16002_METAMASK_SIGNATURE);
//        }
//
//        // 修改
//        UcUser ucUser = UcUser.builder()
//                .id(uId)
//                .publicAddress(publicAddress)
//                .nonce(genMetamaskAuth.getNonce())
//                .updatedAt(currentTime)
//                .build();
//        this.updateById(ucUser);
//
//        ucSecurityStrategyMapper.insert(UcSecurityStrategy.builder()
//                .needAuth(true)
//                .uid(ucUser.getId())
//                .authType(ClientAuthTypeEnum.METAMASK)
//                .createdAt(currentTime)
//                .updatedAt(currentTime)
//                .build());

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
     * @param resetPasswordReq
     */
    @Override
    public void forgotPasswordReset(ResetPasswordReq resetPasswordReq) {
        String account = resetPasswordReq.getAccount();
        String salt = RandomUtil.getRandomSalt();
        String password = PasswordUtil.getPassword(resetPasswordReq.getPassword(), salt);
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
        UcUser user = this.getById(twoFactorAuth.getUserId());
        if (twoFactorAuth != null && twoFactorAuth.getAuthAccountName() != null) {
            // GA验证
            if (ClientAuthTypeEnum.GA.equals(twoFactorAuth.getAuthType())) {
                if (!googleAuthService.verify(loginReq.getAuthCode(), user.getGaSecret())) {
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
    public UserInfoResp getInfo(LoginUserDTO loginUser) {
        UserInfoResp userInfoResp = UserInfoResp.builder().build();
        Long userId = loginUser.getUserId();
        BeanUtil.copyProperties(this.getById(userId), userInfoResp);
        userInfoResp.setSecurityStrategyList(ucSecurityStrategyMapper.getByUserId(userId));
        return userInfoResp;
    }

    /**
     * 修改昵称
     * @param nickname
     * @param loginUser
     */
    @Override
    public Boolean updateNickname(String nickname, LoginUserDTO loginUser) {
        long currentTime = System.currentTimeMillis();
        return this.updateById(UcUser.builder()
                .id(loginUser.getUserId())
                .nickname(nickname)
                .updatedAt(currentTime)
                .build());
    }

    /**
     * 修改密码
     * @param userId
     * @param password
     * @return
     */
    @Override
    public Boolean updatePassword(Long userId, String password) {
        long currentTimeMillis = System.currentTimeMillis();
        String salt = RandomUtil.getRandomSalt();
        return this.updateById(UcUser.builder()
                        .id(userId)
                        .updatedAt(currentTimeMillis)
                        .salt(salt)
                        .password(PasswordUtil.getPassword(password, salt))
                .build());
    }

    /**
     * 修改邮箱
     * @param email
     * @param loginUser
     * @return
     */
    @Override
    public Boolean updateEmail(String email, LoginUserDTO loginUser) {
        long currentTimeMillis = System.currentTimeMillis();
        return this.updateById(UcUser.builder()
                .id(loginUser.getUserId())
                .updatedAt(currentTimeMillis)
                .email(email)
                .build());
    }

    /**
     * 删除ga
     * @param ucUser
     */
    @Override
    public void deleteGa(UcUser ucUser) {

        this.update(new LambdaUpdateWrapper<UcUser>()
                .set(UcUser::getGaSecret,null)
                .eq(UcUser::getId, ucUser.getId())
        );

        ucSecurityStrategyMapper.delete(new QueryWrapper<UcSecurityStrategy>().lambda()
                .eq(UcSecurityStrategy::getUid, ucUser.getId())
                .eq(UcSecurityStrategy::getAuthType, ClientAuthTypeEnum.GA)
        );
    }

    /**
     * 验证是否有ga
     * @param userId
     * @return
     */
    @Override
    public Boolean verifyGa(Long userId) {
        UcSecurityStrategy ucSecurityStrategy = ucSecurityStrategyMapper.selectOne(new QueryWrapper<UcSecurityStrategy>().lambda()
                .eq(UcSecurityStrategy::getUid, userId)
                .eq(UcSecurityStrategy::getNeedAuth, true)
                .eq(UcSecurityStrategy::getAuthType, ClientAuthTypeEnum.GA));
        if (ucSecurityStrategy != null) {
            return true;
        }
        return false;
    }


}
