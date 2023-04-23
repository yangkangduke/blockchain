package com.seeds.uc.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.codec.Base58;
import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.portto.solana.web3.util.TweetNaclFast;
import com.seeds.admin.dto.request.RandomCodeUseReq;
import com.seeds.admin.dto.response.ProfileInfoResp;
import com.seeds.admin.enums.WhetherEnum;
import com.seeds.admin.feign.RemoteGameService;
import com.seeds.admin.feign.RemoteRandomCodeService;
import com.seeds.common.dto.GenericDto;
import com.seeds.common.enums.RandomCodeType;
import com.seeds.common.web.context.UserContext;
import com.seeds.uc.dto.UserDto;
import com.seeds.uc.dto.redis.*;
import com.seeds.uc.dto.request.*;
import com.seeds.uc.dto.response.LoginResp;
import com.seeds.uc.dto.response.UcUserResp;
import com.seeds.uc.dto.response.UserInfoResp;
import com.seeds.uc.dto.response.UserRegistrationResp;
import com.seeds.uc.enums.*;
import com.seeds.uc.exceptions.GenericException;
import com.seeds.uc.exceptions.InvalidArgumentsException;
import com.seeds.uc.exceptions.LoginException;
import com.seeds.uc.mapper.UcSecurityStrategyMapper;
import com.seeds.uc.mapper.UcUserMapper;
import com.seeds.uc.model.UcFile;
import com.seeds.uc.model.UcSecurityStrategy;
import com.seeds.uc.model.UcUser;
import com.seeds.uc.service.*;
import com.seeds.uc.util.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.web3j.crypto.WalletUtils;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

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

    @Value("${Use-invite-code-flag:false}")
    private Boolean inviteFlag;

    @Autowired
    private CacheService cacheService;
    @Autowired
    private UcSecurityStrategyMapper ucSecurityStrategyMapper;
    @Autowired
    private IGoogleAuthService googleAuthService;
    @Autowired
    private SendCodeService sendCodeService;
    @Autowired
    private IUsUserLoginLogService userLoginLogService;
    @Autowired
    private RemoteGameService adminRemoteGameService;
    @Autowired
    private RemoteRandomCodeService remoteRandomCodeService;
    @Autowired
    private KafkaTemplate kafkaTemplate;
    @Autowired
    private IUcFileService ucFileService;
    @Autowired
    MessageSource messageSource;

    /**
     * 注册邮箱账号
     *
     * @param registerReq
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public LoginResp registerEmail(RegisterReq registerReq) {
        String email = registerReq.getEmail();
        sendCodeService.verifyEmailWithUseType(registerReq.getEmail(), registerReq.getCode(), AuthCodeUseTypeEnum.REGISTER);
        // 校验账号重复
        UcUser one = this.getOne(new QueryWrapper<UcUser>().lambda().eq(UcUser::getEmail, email));
        if (one != null) {
            throw new InvalidArgumentsException(UcErrorCodeEnum.ERR_10061_EMAIL_ALREADY_BEEN_USED, messageSource.getMessage("ERR_10061_EMAIL_ALREADY_BEEN_USED", null, LocaleContextHolder.getLocale()));
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

        ucUser.setInviteCode(InviteCode.gen(id));
        updateById(ucUser);

        // 消耗邀请码
        registerWriteOffsInviteCode(registerReq.getInviteCode(), id.toString(), WhetherEnum.YES.value());

        ucSecurityStrategyMapper.insert(UcSecurityStrategy.builder()
                .uid(id)
                .needAuth(true)
                .authType(ClientAuthTypeEnum.EMAIL)
                .createdAt(currentTime)
                .updatedAt(currentTime)
                .build());

        LoginResp loginResp = buildLoginResponse(id, email);
        loginResp.setAccount(email);
        loginResp.setAuthType(ClientAuthTypeEnum.EMAIL);
        return loginResp;
    }

    /**
     * 账号登陆
     *
     * @return
     */
    @Override
    public LoginResp login(LoginReq loginReq) {
        // 校验账号、密码
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
                    .inviteCode(userDto.getInviteCode())
                    .authType(ClientAuthTypeEnum.GA)
                    .build();
        } else {
            // 发送邮件
            sendCodeService.sendUserCodeByUseType(userDto, AuthCodeUseTypeEnum.LOGIN);

            // 将2FA token存入redis，用户进入等待2FA验证态
            cacheService.put2FAInfoWithTokenAndUserAndAuthType(
                    token,
                    userDto.getUid(),
                    userDto.getEmail(),
                    ClientAuthTypeEnum.EMAIL);

            return LoginResp.builder()
                    .token(token)
                    .inviteCode(userDto.getInviteCode())
                    .authType(ClientAuthTypeEnum.EMAIL)
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
    public LoginResp metamaskLogin(MetamaskVerifyReq metamaskVerifyReq) {
        String publicAddress = metamaskVerifyReq.getPublicAddress();
        this.verifyMetamask(metamaskVerifyReq, null);
        long currentTime = System.currentTimeMillis();

        GenMetamaskAuth genMetamaskAuth = cacheService.getGenerateMetamaskAuth(publicAddress);
        UcUser one = this.getOne(new QueryWrapper<UcUser>().lambda()
                .eq(UcUser::getPublicAddress, publicAddress));
        Long userId;
        String inviteCode;
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

            inviteCode = InviteCode.gen(userId);
            ucUser.setInviteCode(inviteCode);
            updateById(ucUser);

            // 消耗邀请码
            registerWriteOffsInviteCode(metamaskVerifyReq.getInviteCode(), userId.toString(), WhetherEnum.YES.value());
        } else {
            userId = one.getId();
            inviteCode = StringUtils.isEmpty(one.getInviteCode()) ? InviteCode.gen(userId) : one.getInviteCode();
            this.updateById(UcUser.builder()
                    .id(userId)
                    .nonce(genMetamaskAuth.getNonce())
                    .inviteCode(inviteCode)
                    .updatedAt(currentTime)
                    .build());
        }

        UcSecurityStrategy ucSecurityStrategy = ucSecurityStrategyMapper.selectOne(new QueryWrapper<UcSecurityStrategy>().lambda()
                .eq(UcSecurityStrategy::getUid, userId)
                .eq(UcSecurityStrategy::getAuthType, ClientAuthTypeEnum.METAMASK));
        if (ucSecurityStrategy == null) {
            // 新增
            ucSecurityStrategyMapper.insert(UcSecurityStrategy.builder()
                    .needAuth(true)
                    .uid(userId)
                    .authType(ClientAuthTypeEnum.METAMASK)
                    .createdAt(currentTime)
                    .updatedAt(currentTime)
                    .build());
        }
        LoginResp loginResp = buildLoginResponse(userId, publicAddress);
        loginResp.setInviteCode(inviteCode);
        return loginResp;
    }

    /**
     * phantom登陆
     *
     * @return
     */
    @Override
    public LoginResp phantomLogin(PhantomVerifyReq phantomVerifyReq) {
        String publicAddress = phantomVerifyReq.getPublicAddress();
        this.verifyPhantom(phantomVerifyReq, null);
        long currentTime = System.currentTimeMillis();

        GenPhantomAuth genPhantomAuth = cacheService.getGeneratePhantomAuth(publicAddress);
        UcUser one = this.getOne(new QueryWrapper<UcUser>().lambda()
                .eq(UcUser::getPublicAddress, publicAddress));
        Long userId;
        String inviteCode;
        if (one == null) {
            // 新增
            UcUser ucUser = UcUser.builder()
                    .nickname(publicAddress)
                    .publicAddress(publicAddress)
                    .nonce(genPhantomAuth.getNonce())
                    .state(ClientStateEnum.NORMAL)
                    .type(ClientTypeEnum.NORMAL)
                    .createdAt(currentTime)
                    .updatedAt(currentTime)
                    .build();
            this.save(ucUser);
            userId = ucUser.getId();

            inviteCode = InviteCode.gen(userId);
            ucUser.setInviteCode(inviteCode);
            updateById(ucUser);

            // 消耗邀请码
            registerWriteOffsInviteCode(phantomVerifyReq.getInviteCode(), userId.toString(), WhetherEnum.YES.value());
        } else {
            userId = one.getId();
            inviteCode = StringUtils.isEmpty(one.getInviteCode()) ? InviteCode.gen(userId) : one.getInviteCode();
            this.updateById(UcUser.builder()
                    .id(userId)
                    .nonce(genPhantomAuth.getNonce())
                    .inviteCode(inviteCode)
                    .updatedAt(currentTime)
                    .build());
        }

        UcSecurityStrategy ucSecurityStrategy = ucSecurityStrategyMapper.selectOne(new QueryWrapper<UcSecurityStrategy>().lambda()
                .eq(UcSecurityStrategy::getUid, userId)
                .eq(UcSecurityStrategy::getAuthType, ClientAuthTypeEnum.PHANTOM));
        if (ucSecurityStrategy == null) {
            // 新增
            ucSecurityStrategyMapper.insert(UcSecurityStrategy.builder()
                    .needAuth(true)
                    .uid(userId)
                    .authType(ClientAuthTypeEnum.PHANTOM)
                    .createdAt(currentTime)
                    .updatedAt(currentTime)
                    .build());
        }
        LoginResp loginResp = buildLoginResponse(userId, publicAddress);
        loginResp.setInviteCode(inviteCode);
        return loginResp;
    }

    /**
     * 绑定metamask
     * @param uId
     */
    @Override
    public void bindMetamask(AuthTokenDTO authTokenDTO, Long uId) {
        long currentTime = System.currentTimeMillis();
        // 修改
        String[] split = authTokenDTO.getSecret().split(":");
        String nonce =  split[2].replace("\n","");
        UcUser ucUser = UcUser.builder()
                .id(uId)
                .publicAddress(authTokenDTO.getAccountName())
                .nonce(nonce)
                .updatedAt(currentTime)
                .build();
        this.updateById(ucUser);

        ucSecurityStrategyMapper.insert(UcSecurityStrategy.builder()
                .needAuth(true)
                .uid(ucUser.getId())
                .authType(ClientAuthTypeEnum.METAMASK)
                .createdAt(currentTime)
                .updatedAt(currentTime)
                .build());

    }

    /**
     * 绑定phantom
     * @param uId
     */
    @Override
    public void bindPhantom(AuthTokenDTO authTokenDTO, Long uId) {
        long currentTime = System.currentTimeMillis();
        // 修改
        String[] split = authTokenDTO.getSecret().split(":");
        String nonce =  split[6].replace("\nIssued At","");
        UcUser ucUser = UcUser.builder()
                .id(uId)
                .publicAddress(authTokenDTO.getAccountName())
                .nonce(nonce)
                .updatedAt(currentTime)
                .build();
        this.updateById(ucUser);

        ucSecurityStrategyMapper.insert(UcSecurityStrategy.builder()
                .needAuth(true)
                .uid(ucUser.getId())
                .authType(ClientAuthTypeEnum.PHANTOM)
                .createdAt(currentTime)
                .updatedAt(currentTime)
                .build());

    }


    /**
     * 忘记密码-验证
     *
     * @param code
     */
    @Override
    public void forgotPasswordVerify(String code, String email) {
        AuthCodeDTO authCode = cacheService.getAuthCode(email, AuthCodeUseTypeEnum.RESET_PASSWORD, ClientAuthTypeEnum.EMAIL);
        if (authCode == null || !code.equals(authCode.getCode())) {
            throw new InvalidArgumentsException(UcErrorCodeEnum.ERR_17000_EMAIL_VERIFICATION_FAILED, messageSource.getMessage("ERR_17000_EMAIL_VERIFICATION_FAILED", null, LocaleContextHolder.getLocale()));
        }
        // 查看email是否存在
        UcUser one = this.getOne(new LambdaQueryWrapper<UcUser>().eq(UcUser::getEmail, email));
        if (one == null) {
            throw new InvalidArgumentsException(UcErrorCodeEnum.ERR_10001_ACCOUNT_YET_NOT_REGISTERED, messageSource.getMessage("ERR_10001_ACCOUNT_YET_NOT_REGISTERED", null, LocaleContextHolder.getLocale()));
        }

    }

    /**
     * 忘记密码-重置密码
     *
     * @param resetPasswordReq
     */
    @Override
    public LoginResp forgotPasswordReset(ResetPasswordReq resetPasswordReq) {
        String email = resetPasswordReq.getEmail();
        String salt = RandomUtil.getRandomSalt();
        String password = PasswordUtil.getPassword(resetPasswordReq.getPassword(), salt);
        UcUser one = this.getOne(new LambdaQueryWrapper<UcUser>()
                .eq(UcUser::getEmail, email));
        UcUser user = UcUser.builder()
                .password(password)
                .salt(salt)
                .id(one.getId())
                .build();
        this.updateById(user);
        Long userId = user.getId();

        LoginResp loginResp = buildLoginResponse(userId, email);
        loginResp.setAuthType(ClientAuthTypeEnum.EMAIL);
        loginResp.setAccount(email);
        return loginResp;
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
        if (twoFactorAuth == null) {
            throw new LoginException(UcErrorCodeEnum.ERR_10023_TOKEN_EXPIRED, messageSource.getMessage("ERR_10023_TOKEN_EXPIRED", null, LocaleContextHolder.getLocale()));
        }
        UcUser user = this.getById(twoFactorAuth.getUserId());
        if (twoFactorAuth != null && twoFactorAuth.getAuthAccountName() != null) {
            // GA验证
            if (ClientAuthTypeEnum.GA.equals(twoFactorAuth.getAuthType())) {
                if (!googleAuthService.verify(loginReq.getAuthCode(), user.getGaSecret())) {
                    throw new LoginException(UcErrorCodeEnum.ERR_10088_WRONG_GOOGLE_AUTHENTICATOR_CODE, messageSource.getMessage("ERR_10088_WRONG_GOOGLE_AUTHENTICATOR_CODE", null, LocaleContextHolder.getLocale()));
                }
            } else if (ClientAuthTypeEnum.EMAIL.equals(twoFactorAuth.getAuthType())) {
                // 邮箱验证
                AuthCodeDTO authCode = cacheService.getAuthCode(
                                twoFactorAuth.getAuthAccountName(),
                                AuthCodeUseTypeEnum.LOGIN,
                                twoFactorAuth.getAuthType());

                log.info("verifyTwoFactorLogin - in redis twoFactorAuth:{} and authCode: {}", twoFactorAuth, authCode);
                if (authCode == null) {
                    throw new LoginException(UcErrorCodeEnum.ERR_10036_AUTH_CODE_EXPIRED, messageSource.getMessage("ERR_10036_AUTH_CODE_EXPIRED", null, LocaleContextHolder.getLocale()));
                }
                if (!authCode.getCode().equals(loginReq.getAuthCode())) {
                    throw new LoginException(UcErrorCodeEnum.ERR_10033_WRONG_EMAIL_CODE, messageSource.getMessage("ERR_10033_WRONG_EMAIL_CODE", null, LocaleContextHolder.getLocale()));
                }

            } else {
                throw new LoginException(UcErrorCodeEnum.ERR_504_MISSING_ARGUMENTS, messageSource.getMessage("ERR_504_MISSING_ARGUMENTS", null, LocaleContextHolder.getLocale()));
            }

        } else {
            throw new LoginException(UcErrorCodeEnum.ERR_10023_TOKEN_EXPIRED, messageSource.getMessage("ERR_10023_TOKEN_EXPIRED", null, LocaleContextHolder.getLocale()));
        }
//        this.sendLoginMsg(twoFactorAuth.getAuthAccountName(), twoFactorAuth.getUserIp(), twoFactorAuth.getServiceRegion());
        LoginResp loginResp = buildLoginResponse(user.getId(), twoFactorAuth.getAuthAccountName());
        loginResp.setInviteCode(user.getInviteCode());
        return loginResp;
    }

    @Override
    public void sendLoginMsg(String email, String userIp, String serviceRegion){
        // 生产登陆成功消息
        try {
            Map loginMap = new HashMap<>();
            loginMap.put("email", email);
            loginMap.put("userIp", userIp);
            loginMap.put("serviceRegion", serviceRegion);
            ListenableFuture<SendResult> listenableFuture = kafkaTemplate.send("login_topic", loginMap.toString());
            // 提供回调方法，可以监控消息的成功或失败的后续处理
            listenableFuture.addCallback(new ListenableFutureCallback<SendResult>() {
                @Override
                public void onFailure(Throwable throwable) {
                    log.info("发送消息失败，" + throwable.getMessage());
                }
                @Override
                public void onSuccess(SendResult sendResult) {
                    // 消息发送到的topic
                    String topic = sendResult.getRecordMetadata().topic();
                    // 消息发送到的分区
                    int partition = sendResult.getRecordMetadata().partition();
                    // 消息在分区内的offset
                    long offset = sendResult.getRecordMetadata().offset();
                    log.info(String.format("发送消息成功，topc：%s, partition: %s, offset：%s ", topic, partition, offset));
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getPublicAddress(Long id) {
        UcUser user = getById(id);
        if (user == null) {
            return null;
        }
        return user.getPublicAddress();
    }

    @Override
    public UcUserResp getByPublicAddress(String publicAddress) {
        UcUser user = getOne(new QueryWrapper<UcUser>().lambda()
                .eq(UcUser::getPublicAddress, publicAddress));
        if (user == null) {
            return null;
        }
        UcUserResp userResp = new UcUserResp();
        BeanUtils.copyProperties(user, userResp);
        return userResp;
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
            throw new InvalidArgumentsException(UcErrorCodeEnum.ERR_10001_ACCOUNT_YET_NOT_REGISTERED, messageSource.getMessage("ERR_10001_ACCOUNT_YET_NOT_REGISTERED", null, LocaleContextHolder.getLocale()));
        }
        String loginPassword = PasswordUtil.getPassword(password, ucUser.getSalt());
        if (!loginPassword.equals(ucUser.getPassword())) {
            throw new InvalidArgumentsException(UcErrorCodeEnum.ERR_10013_ACCOUNT_NAME_PASSWORD_INCORRECT, messageSource.getMessage("ERR_10013_ACCOUNT_NAME_PASSWORD_INCORRECT", null, LocaleContextHolder.getLocale()));
        }
        UserDto userDto = new UserDto();
        BeanUtil.copyProperties(ucUser, userDto);
        userDto.setAuthType(ClientAuthTypeEnum.EMAIL);
        userDto.setUid(ucUser.getId());
        // 旧用户生成邀请码
        if (StringUtils.isEmpty(ucUser.getInviteCode())) {
            ucUser.setInviteCode(InviteCode.gen(ucUser.getId()));
            updateById(ucUser);
        }
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
        if (StringUtils.isNotBlank(userInfoResp.getAvatar())){
            UcFile file = ucFileService.getOne(new QueryWrapper<UcFile>().lambda().eq(UcFile::getObjectName, userInfoResp.getAvatar()));
            userInfoResp.setAvatarUrl(String.format("/uc/public/file/%s/%s", file.getBucketName(), userInfoResp.getAvatar()));
        }

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
     * 修改头像
     * @param avatar
     * @param loginUser
     * @return
     */
    @Override
    public Boolean updateAvatar(String avatar, LoginUserDTO loginUser) throws Exception {
        // 查询原来的头像，如果有就先删除然后在添加
        Long userId = loginUser.getUserId();
        UcUser user = this.getById(userId);
        String oldAvatar = user.getAvatar();
        if (StringUtils.isNotBlank(oldAvatar)) {
            ucFileService.deleteFileByName(oldAvatar);
        }

        long currentTime = System.currentTimeMillis();
        return this.updateById(UcUser.builder()
                .id(userId)
                .avatar(avatar)
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


    /**
     * 验证Phantom签名
     * @param verifyReq
     * @return
     */
    @Override
    public Boolean verifyPhantom(PhantomVerifyReq verifyReq, HttpServletRequest request) {
        String publicAddress = verifyReq.getPublicAddress();
        String signature = verifyReq.getSignature();
        String message = verifyReq.getMessage();
        String[] split = message.split(":");
        String nonce =  split[6].replace("\nIssued At","");

        // 认证、绑定钱包的时候用到；登陆和注册不进入此方法
        if (request != null) {
            LoginUserDTO loginUser = cacheService.getUserByToken(WebUtil.getTokenFromRequest(request));
            UcUser ucUser = this.getById(loginUser.getUserId());
            // 无钱包地址，非认证操作，判断地址是否已使用
            if (ucUser.getPublicAddress() == null) {
                UcUser otherUser = this.getOne(new QueryWrapper<UcUser>().lambda()
                        .eq(UcUser::getPublicAddress, verifyReq.getPublicAddress()));
                // 判断地址是否已经被绑定过了
                if (otherUser != null) {
                    throw new InvalidArgumentsException(UcErrorCodeEnum.ERR_17006_PHANTOM_ADDRESS_BINDED, messageSource.getMessage("ERR_17006_PHANTOM_ADDRESS_BINDED", null, LocaleContextHolder.getLocale()));
                }
            } else {
                // 存在钱包地址，无换绑钱包功能，为认证操作，判断地址是否一致
                if (!publicAddress.equals(ucUser.getPublicAddress())) {
                    throw new InvalidArgumentsException(UcErrorCodeEnum.ERR_17005_PHANTOM_ADDRESS_NOT_MATCH, messageSource.getMessage("ERR_17005_PHANTOM_ADDRESS_NOT_MATCH", null, LocaleContextHolder.getLocale()));
                }
            }
        }

        GenPhantomAuth genPhantomAuth = cacheService.getGeneratePhantomAuth(verifyReq.getPublicAddress());
        if (genPhantomAuth == null || StringUtils.isBlank(genPhantomAuth.getNonce()) || !genPhantomAuth.getNonce().equals(nonce) ) {
            throw new InvalidArgumentsException(UcErrorCodeEnum.ERR_17003_PHANTOM_NONCE_EXPIRED, messageSource.getMessage("ERR_17003_PHANTOM_NONCE_EXPIRED", null, LocaleContextHolder.getLocale()));
        }
        // 校验签名信息
        try{
            if (!new TweetNaclFast.Signature(Base58.decode(publicAddress), null).detached_verify(message.getBytes(StandardCharsets.UTF_8), Base58.decode(signature))) {
                throw new InvalidArgumentsException(UcErrorCodeEnum.ERR_17002_PHANTOM_SIGNATURE, messageSource.getMessage("ERR_17002_PHANTOM_SIGNATURE", null, LocaleContextHolder.getLocale()));
            }

        } catch (Exception e) {
            throw new InvalidArgumentsException(UcErrorCodeEnum.ERR_17002_PHANTOM_SIGNATURE, messageSource.getMessage("ERR_17002_PHANTOM_SIGNATURE", null, LocaleContextHolder.getLocale()));
        }
        return true;
    }

    /**
     * 验证metamask签名
     * @param verifyReq
     * @return
     */
    @Override
    public Boolean verifyMetamask(MetamaskVerifyReq verifyReq, HttpServletRequest request) {
        String publicAddress = verifyReq.getPublicAddress();
        String signature = verifyReq.getSignature();
        String message = verifyReq.getMessage();
        String[] split = message.split(":");
        String nonce =  split[2].replace("\n","");

        // 认证、绑定钱包的时候用到；登陆和注册不进入此方法
        if (request != null) {
            LoginUserDTO loginUser = cacheService.getUserByToken(WebUtil.getTokenFromRequest(request));
            UcUser ucUser = this.getById(loginUser.getUserId());
            UcUser otherUser = this.getOne(new QueryWrapper<UcUser>().lambda()
                    .eq(UcUser::getPublicAddress, verifyReq.getPublicAddress())
                    .ne(UcUser::getId, loginUser.getUserId()));
            // 判断地址是否已经被绑定过了
            if (otherUser != null) {
                throw new InvalidArgumentsException(UcErrorCodeEnum.ERR_16006_METAMASK_ADDRESS_BINDED, messageSource.getMessage("ERR_16006_METAMASK_ADDRESS_BINDED", null, LocaleContextHolder.getLocale()));
            }
            // 判断地址跟数据库地址是否一致
            if (!publicAddress.equals(ucUser.getPublicAddress())) {
                throw new InvalidArgumentsException(UcErrorCodeEnum.ERR_16005_METAMASK_ADDRESS_NOT_MATCH, messageSource.getMessage("ERR_16005_METAMASK_ADDRESS_NOT_MATCH", null, LocaleContextHolder.getLocale()));
            }
        }

        GenMetamaskAuth genMetamaskAuth = cacheService.getGenerateMetamaskAuth(verifyReq.getPublicAddress());
        if (genMetamaskAuth == null || StringUtils.isBlank(genMetamaskAuth.getNonce()) || !genMetamaskAuth.getNonce().equals(nonce) ) {
            throw new InvalidArgumentsException(UcErrorCodeEnum.ERR_16003_METAMASK_NONCE_EXPIRED, messageSource.getMessage("ERR_16003_METAMASK_NONCE_EXPIRED", null, LocaleContextHolder.getLocale()));
        }
        // 地址合法性校验
        if (!WalletUtils.isValidAddress(publicAddress)) {
            // 不合法直接返回错误
            throw new InvalidArgumentsException(UcErrorCodeEnum.ERR_16001_METAMASK_ADDRESS, messageSource.getMessage("ERR_16001_METAMASK_ADDRESS", null, LocaleContextHolder.getLocale()));
        }
        // 校验签名信息
        try{
            if (!CryptoUtils.validate(signature, message, publicAddress)) {
                throw new InvalidArgumentsException(UcErrorCodeEnum.ERR_16002_METAMASK_SIGNATURE, messageSource.getMessage("ERR_16002_METAMASK_SIGNATURE", null, LocaleContextHolder.getLocale()));
            }
        } catch (Exception e) {
            throw new InvalidArgumentsException(UcErrorCodeEnum.ERR_16002_METAMASK_SIGNATURE, messageSource.getMessage("ERR_16002_METAMASK_SIGNATURE", null, LocaleContextHolder.getLocale()));
        }
        return true;
    }

    @Override
    public Map<Long, String> queryNameByIds(Collection<Long> ids) {
        List<UcUser> list = listByIds(ids);
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyMap();
        }
        return list.stream().collect(Collectors.toMap(UcUser::getId, UcUser::getNickname));
    }

    @Override
    public Map<Long, String> queryEmailByIds(Collection<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return Collections.emptyMap();
        }
        List<UcUser> list = listByIds(ids);
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyMap();
        }
        return list.stream().collect(Collectors.toMap(UcUser::getId, UcUser::getEmail));
    }

    @Override
    public LoginResp buildLoginResponse(Long userId, String email) {
        // 生成uc token给用户
        String ucToken = RandomUtil.genRandomToken(userId.toString());
        // 将token存入redis，用户进入登陆态
        cacheService.putUserWithTokenAndLoginName(ucToken, userId, email);

        // 生成refresh token给用户
        String refreshToken = RandomUtil.genRandomToken(ucToken);
        // 将refresh token存入redis
        cacheService.putUserRefreshToken(refreshToken, userId, email);

        // 登录成功，记录日志
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String realClientIp = WebUtil.getIpAddr(request);
        userLoginLogService.recordLog(userId, email, realClientIp);

        return LoginResp.builder()
                .ucToken(ucToken)
                .refreshToken(refreshToken)
                .build();
    }

    /**
     * 校验邮箱
     * @param email
     * @return
     */
    @Override
    public Boolean registerCheckEmail(String email) {
        // 校验账号重复
        UcUser one = this.getOne(new QueryWrapper<UcUser>().lambda().eq(UcUser::getEmail, email));
        if (one != null) {
            return false;
        }
        return true;
    }

    @Override
    public void registerWriteOffsInviteCode(String inviteCode, String userIdentity, Integer useFlag) {
        // 邀请码校验开关
        if (!inviteFlag) {
            return;
        }
        if (StringUtils.isEmpty(inviteCode)) {
            throw new InvalidArgumentsException(messageSource.getMessage("ERR_11501_INVITATION_CODE_NOT_EXIST", null, LocaleContextHolder.getLocale()));
        }
        RandomCodeUseReq req = new RandomCodeUseReq();
        req.setUseFlag(useFlag);
        req.setUserIdentity(userIdentity);
        req.setType(RandomCodeType.INVITE.getCode());
        req.setCode(inviteCode);
        GenericDto<Object> result = remoteRandomCodeService.useRandomCode(req);
        if (!result.isSuccess()) {
            throw new InvalidArgumentsException(messageSource.getMessage("ERR_11501_INVITATION_CODE_NOT_EXIST", null, LocaleContextHolder.getLocale()));
        }
    }

    @Override
    public Boolean inviteFlag(String account) {
        if (StringUtils.isEmpty(account)) {
            return inviteFlag;
        }
        UcUser one = this.getOne(new QueryWrapper<UcUser>().lambda()
                .eq(UcUser::getPublicAddress, account));
        return inviteFlag && one == null;
    }

    /**
     * 获取所有用户信息
     * @param page
     * @param allUserReq
     * @return
     */
    @Override
    public Page<UcUserResp> getAllUser(Page page, AllUserReq allUserReq) {
        Page<UcUserResp> respPage = baseMapper.getAllUser(page, allUserReq);
        return respPage;
    }

    @Override
    public List<UcUserResp> getUserList(List<Long> ids) {
        List<UcUserResp> result = Lists.newArrayList();
        List<UcUser> list = this.list(new QueryWrapper<UcUser>().lambda().in(UcUser::getId, new HashSet<>(ids)));
        if (!CollectionUtils.isEmpty(list)) {
            result = list.stream().map(p -> {
                UcUserResp ucUserResp = new UcUserResp();
                BeanUtils.copyProperties(p, ucUserResp);
                return ucUserResp;
            }).collect(Collectors.toList());
        }
        return result;
    }

    @Override
    public UserRegistrationResp getUserRegistration() {

        UserRegistrationResp resp = new UserRegistrationResp();
        long total = this.count();
        long todayCount = this.count(new LambdaQueryWrapper<UcUser>().between(UcUser::getCreatedAt, DateUtil.beginOfDay(new Date()).getTime(), DateUtil.endOfDay(new Date()).getTime()));
        resp.setTotalRegisteredUsers(total);
        resp.setTodayRegisteredUsers(todayCount);
        return resp;
    }

    @Override
    public ProfileInfoResp profileInfo(Long userId, Long gameId) {
        if (userId == null) {
            userId = UserContext.getCurrentUserId();
        }
        UcUser user = getById(userId);
        if (user == null || StringUtils.isEmpty(user.getEmail())) {
            throw new GenericException("Game account not exits!");
        }
        // 先从redis缓存中拿个人概括数据
        String data = cacheService.getProfileInfo(userId.toString(), gameId.toString());
        ProfileInfoResp resp = null;
        if (StringUtils.isNotBlank(data)) {
            resp = JSONUtil.toBean(data, ProfileInfoResp.class);
            // 判断是否过期
            if (resp.getExpireTime() > System.currentTimeMillis()) {
                return resp;
            }
        }
        // 请求游戏方获取个人游戏概括数据
        GenericDto<ProfileInfoResp> result;
        try {
            result = adminRemoteGameService.profileInfo(gameId, user.getEmail());
        } catch (Exception e) {
            if (resp == null) {
                throw e;
            }
            return resp;
        }
        if (!result.isSuccess()) {
            if (resp == null) {
                throw new GenericException(result.getMessage());
            }
            return resp;
        }
        // 设置redis个人游戏概括数据
        ProfileInfoResp newData = result.getData();
        if (newData != null) {
            cacheService.putProfileInfo(userId.toString(), gameId.toString(), newData);
        }
        return newData;
    }
}
