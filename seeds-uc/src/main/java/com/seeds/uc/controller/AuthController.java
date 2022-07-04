package com.seeds.uc.controller;

import com.seeds.common.dto.GenericDto;
import com.seeds.uc.dto.UserDto;
import com.seeds.uc.dto.redis.AuthCode;
import com.seeds.uc.dto.redis.LoginUser;
import com.seeds.uc.dto.redis.TwoFactorAuth;
import com.seeds.uc.dto.request.*;
import com.seeds.uc.dto.request.AuthCodeSendReq;
import com.seeds.uc.dto.request.LoginReq;
import com.seeds.uc.dto.request.RegisterCodeVerifyReq;
import com.seeds.uc.dto.request.RegisterReq;
import com.seeds.uc.dto.request.TwoFactorLoginReq;
import com.seeds.uc.dto.response.LoginResp;
import com.seeds.uc.enums.AuthCodeUseTypeEnum;
import com.seeds.uc.enums.ClientAuthTypeEnum;
import com.seeds.uc.enums.UcErrorCode;
import com.seeds.uc.exceptions.LoginException;
import com.seeds.uc.exceptions.SendAuthCodeException;
import com.seeds.uc.mapper.SecurityStrategyMapper;
import com.seeds.uc.mapper.UserMapper;
import com.seeds.uc.model.SecurityStrategy;
import com.seeds.uc.model.User;
import com.seeds.uc.service.*;
import com.seeds.uc.util.RandomUtil;
import com.seeds.uc.util.WebUtil;
import com.seeds.uc.service.CacheService;
import com.seeds.uc.service.GoogleAuthService;
import com.seeds.uc.service.RiskControlService;
import com.seeds.uc.service.SendCodeService;
import com.seeds.uc.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author allen
 * @email allen.hua.ai@gmail.com
 * @date 2020/8/19
 */
@Slf4j
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private CacheService cacheService;

    @Autowired
    private SendCodeService sendCodeService;

    @Autowired
    private RiskControlService riskControlService;

    @Autowired
    private SecurityStrategyMapper securityStrategyMapper;

    @Autowired
    private GoogleAuthService googleAuthService;

    /**
     * 调用方法/risk/control, 根据返回结果确定是否要调用验证码以及调用何种验证码.
     * 调用方法/email_code/send或者/sms_code/send, 参数use_type=REGISTER, 发送注册验证码.
     * 调用方法/register进行注册.
     */
    @PostMapping("/register")
    public GenericDto<LoginResp> register(@RequestBody RegisterReq registerReq) {
        if (StringUtils.isBlank(registerReq.getEmail())
                && StringUtils.isBlank(registerReq.getPhone())) {
            throw new LoginException(UcErrorCode.ERR_10026_PHONE_EMAIL_CANNOT_BOTH_EMPTY);
        }
        if (StringUtils.isNotBlank(registerReq.getEmail())
                && StringUtils.isNotBlank(registerReq.getPhone())) {
            throw new LoginException(UcErrorCode.ERR_10027_CHOOSE_PHONE_OR_EMAIL);
        }
        ClientAuthTypeEnum authTypeEnum =
                StringUtils.isBlank(registerReq.getEmail())
                        ? ClientAuthTypeEnum.PHONE : ClientAuthTypeEnum.EMAIL;

        String loginName =
                ClientAuthTypeEnum.getAccountNameByAuthType(registerReq.getPhone(), registerReq.getEmail(), authTypeEnum);

        // 前端已经调用了发送验证码，现在去redis里查有没有
        AuthCode authCode =
                cacheService.getAuthCode(
                        loginName,
                        AuthCodeUseTypeEnum.REGISTER,
                        authTypeEnum);

        // 验证otp是否正确
        if (authCode == null
                || authCode.getCode() == null
                || !authCode.getCode().equals(registerReq.getAuthCode())) {
            UcErrorCode errorCode =
                    ClientAuthTypeEnum.PHONE.equals(authTypeEnum)
                            ? UcErrorCode.ERR_10032_WRONG_SMS_CODE
                            : UcErrorCode.ERR_10033_WRONG_EMAIL_CODE;
            throw new LoginException(errorCode);
        }

        // 验证通过，生成密码，insert新user
        UserDto userDto = userService.register(authTypeEnum, registerReq);

        // 插入security strategy
        securityStrategyMapper.insert(
                SecurityStrategy.builder()
                        .authType(authTypeEnum)
                        .createdAt(System.currentTimeMillis())
                        .needAuth(true)
                        .uid(userDto.getUid())
                        .updatedAt(System.currentTimeMillis())
                        .build());

        // 注册完成，生成uc token给用户
        String ucToken = RandomUtil.genRandomToken(userDto.getUid().toString());

        // 将token存入redis，用户进入登陆态
        cacheService.putUserWithTokenAndLoginName(ucToken, userDto.getUid(), loginName);

        return GenericDto.success(
                LoginResp.builder()
                        .ucToken(ucToken)
                        .build());
    }

    /**
     * 1. risk control 根据返回确定是否要调用验证码及调用哪种验证码 captcha
     * 2. 调用/uc/open/login做登陆
     * 3. 根据返回2FA方式，调用sendSMS/sendEmail
     * 3. 如果需要二次验证，调用2fa/login 带token做二次验证登陆
     * 4. 返回token
     * <p>
     * 如果有GA，没有GA则使用用户选择的剩余唯一项，用户手机邮箱都开着则使用用户登陆项
     * 二次验证： 对于手机验证码和邮箱验证码，不需要客户端调用验证码发送接口，后台会自动下发验证码
     *
     * @param loginReq
     * @return
     */
    @PostMapping("/login")
    public GenericDto<LoginResp> login(@RequestBody LoginReq loginReq) {
        // 检查用户名密码，如果密码正确，返回用户信息
        UserDto userDto = userService.verifyLogin(loginReq);

        ClientAuthTypeEnum loginType = userDto.getAuthType();

        String loginName =
                ClientAuthTypeEnum.getAccountNameByAuthType(
                        userDto.getPhone(),
                        userDto.getEmail(),
                        userDto.getAuthType());

        boolean need2FA = riskControlService.checkNeed2FA(userDto);
        // 如果需要2FA而且是手机或者邮箱验证，则不需要前端请求来发code，后端会直接发
        if (need2FA) {
            // 产生2fa验证的token，用户进入2FA登陆阶段，前端再次call 2FA登陆接口需要带上2FA token
            String token = RandomUtil.genRandomToken(userDto.getUid().toString());
            //检查用户现在有没有GA
            List<SecurityStrategy> securityStrategyList =
                    securityStrategyMapper.listByUid(userDto.getUid());
            Map<ClientAuthTypeEnum, SecurityStrategy> strategyEnumMap =
                    securityStrategyList
                            .stream()
                            .collect(Collectors.toMap(SecurityStrategy::getAuthType, v -> v));
            Optional<SecurityStrategy> gaSecurityStrategy =
                    Optional.ofNullable(strategyEnumMap.get(ClientAuthTypeEnum.GA));

            LoginResp loginResp;
            if (gaSecurityStrategy.isPresent() && gaSecurityStrategy.get().getNeedAuth()) {

                // 将2FA token存入redis，用户进入等待2FA验证态
                cacheService.put2FAInfoWithTokenAndUserAndAuthType(
                        token,
                        userDto.getUid(),
                        loginName,
                        ClientAuthTypeEnum.GA);
                loginResp =
                        LoginResp.builder()
                                .token(token)
                                .type(ClientAuthTypeEnum.GA)
                                .phone(userDto.getPhone()).build();
            } else {
                // 将2FA token存入redis，用户进入等待2FA验证态
                cacheService.put2FAInfoWithTokenAndUserAndAuthType(
                        token,
                        userDto.getUid(),
                        loginName,
                        userDto.getAuthType());
                // 向用户发送2FA验证码，并将验证码存入redis待验证
                // TODO  检查用户选择的发送验证码的形式, 默认GA，否则看用户开放了哪个，
                sendCodeService.sendUserCodeByUseType(userDto, AuthCodeUseTypeEnum.LOGIN);
                loginResp =
                        LoginResp.builder()
                                .token(token)
                                .type(userDto.getAuthType())
                                .phone(userDto.getPhone())
                                .email(userDto.getEmail())
                                .build();
            }

            return GenericDto.failure(null, UcErrorCode.ERR_10070_PLEASE_ENTER_2FA.getCode(), loginResp);
        } else {
            // 不需要2FA验证，直接下发uc token
            String ucToken = RandomUtil.genRandomToken(userDto.getUid().toString());
            // 将产生的uc token存入redis
            cacheService.putUserWithTokenAndLoginName(ucToken, userDto.getUid(), loginName);

            return GenericDto.success(LoginResp.builder()
                    .ucToken(ucToken)
                    .build());
        }
    }

    @PostMapping("/2fa/login")
    public GenericDto<LoginResp> twoFactorCheck(@RequestBody TwoFactorLoginReq loginReq) {
        log.info("verifyTwoFactorLogin: {}", loginReq);
        AuthCode authCode;
        TwoFactorAuth twoFactorAuth = cacheService.get2FAInfoWithToken(loginReq.getToken());
        if (twoFactorAuth != null
                && twoFactorAuth.getAuthAccountName() != null) {
            if (ClientAuthTypeEnum.GA.equals(twoFactorAuth.getAuthType())) {
                if (!googleAuthService.verifyUserCode(twoFactorAuth.getUserId(), loginReq.getAuthCode())) {
                    throw new LoginException(UcErrorCode.ERR_10088_WRONG_GOOGLE_AUTHENTICATOR_CODE);
                }
            } else {
                authCode =
                        cacheService.getAuthCode(
                                twoFactorAuth.getAuthAccountName(),
                                AuthCodeUseTypeEnum.LOGIN,
                                twoFactorAuth.getAuthType());
                log.info("verifyTwoFactorLogin - in redis twoFactorAuth:{} and authCode: {}", twoFactorAuth, authCode);
                if (authCode == null) {
                    UcErrorCode ucErrorCode =
                            ClientAuthTypeEnum.PHONE.equals(twoFactorAuth.getAuthType())
                                    ? UcErrorCode.ERR_10034_SMS_CODE_EXPIRED
                                    : UcErrorCode.ERR_10036_AUTH_CODE_EXPIRED;
                    throw new LoginException(ucErrorCode);
                }
                if (!authCode.getCode().equals(loginReq.getAuthCode())) {
                    throw new LoginException(UcErrorCode.ERR_10032_WRONG_SMS_CODE);
                }
            }
        } else {
            throw new LoginException(UcErrorCode.ERR_10070_PLEASE_ENTER_2FA);
        }

        User user = userMapper.selectByPrimaryKey(twoFactorAuth.getUserId());
        // TODO need to refactor, userDto应该只有必须的可以拿到的字段，需要手动set的字段要想办法解决而不是imply，可能为空

        // 用户验证通过，产生uc token
        String ucToken = RandomUtil.genRandomToken(user.getId().toString());

        // 将产生的uc token存入redis
        cacheService.putUserWithTokenAndLoginName(ucToken, user.getId(), twoFactorAuth.getAuthAccountName());

        return GenericDto.success(
                LoginResp.builder()
                        .ucToken(ucToken)
                        .build());
    }

    @PostMapping("sms/send")
    public GenericDto<Object> sendSmsCode(@RequestBody AuthCodeSendReq sendReq, HttpServletRequest request) {
        log.info("AuthController - sendSmsCode got request: {}", sendReq);
        if (AuthCodeUseTypeEnum.CODE_NO_NEED_LOGIN_READ_REQUEST.contains(sendReq.getUseType())) {
            // 不需要登陆，请求里带手机号，如: REGISTER
            sendCodeService.sendSmsWithUseType(sendReq.getCountryCode(), sendReq.getPhone(), sendReq.getUseType());
        } else if (AuthCodeUseTypeEnum.CODE_NO_NEED_LOGIN_READ_DB.contains(sendReq.getUseType())) {
            // 需要token(拒绝登陆后发的用于2FA的token)，手机号从数据库查, 如：LOGIN
            sendCodeService.sendSmsWithTokenAndUseType(sendReq.getToken(), sendReq.getUseType());
        } else {
            // 需要登陆
            String loginToken = WebUtil.getTokenFromRequest(request);
            LoginUser loginUser = cacheService.getUserByToken(loginToken);

            if (StringUtils.isBlank(loginToken) || loginUser == null) {
                throw new SendAuthCodeException(UcErrorCode.ERR_401_NOT_LOGGED_IN);
            }

            if (AuthCodeUseTypeEnum.SMS_NEED_LOGIN_READ_REQUEST_SET.contains(sendReq.getUseType())) {
                // 需要登陆, 且手机号从请求中读，如： REBIND_PHONE
                sendCodeService.sendSmsWithUseType(sendReq.getCountryCode(), sendReq.getPhone(), sendReq.getUseType());
            } else if (AuthCodeUseTypeEnum.SMS_NEED_LOGIN_READ_DB_SET.contains(sendReq.getUseType())) {
                // 需要登陆，但是手机号从DB里读
                UserDto userDto = userService.getUserByUid(loginUser.getUserId());

                sendCodeService.sendSmsWithUseType(userDto.getCountryCode(), userDto.getPhone(), sendReq.getUseType());
            } else {
                // 没有被录入的类型，直接抛错误，后面再加
                throw new SendAuthCodeException(UcErrorCode.ERR_502_ILLEGAL_ARGUMENTS);
            }
        }

        return GenericDto.success(null);
    }

    @PostMapping("email/send")
    public GenericDto<Object> sendEmailCode(@RequestBody AuthCodeSendReq sendReq, HttpServletRequest request) {
        log.info("AuthController - sendEmailCode got request: {}", sendReq);
        if (AuthCodeUseTypeEnum.CODE_NO_NEED_LOGIN_READ_REQUEST.contains(sendReq.getUseType())) {
            // 不需要登陆，请求里带邮箱，如: REGISTER
            sendCodeService.sendEmailWithUseType(sendReq.getEmail(), sendReq.getUseType());
        } else if (AuthCodeUseTypeEnum.CODE_NO_NEED_LOGIN_READ_DB.contains(sendReq.getUseType())) {
            // 需要token(拒绝登陆后发的用于2FA的token)，邮箱从数据库查, 如：LOGIN
            sendCodeService.sendEmailWithTokenAndUseType(sendReq.getToken(), sendReq.getUseType());
        } else {
            // 需要登陆
            String loginToken = WebUtil.getTokenFromRequest(request);
            LoginUser loginUser = cacheService.getUserByToken(loginToken);

            if (StringUtils.isBlank(loginToken) || loginUser == null) {
                throw new SendAuthCodeException(UcErrorCode.ERR_401_NOT_LOGGED_IN);
            }
            // TODO 邮箱需要检查是否已使用
            if (AuthCodeUseTypeEnum.EMAIL_NEED_LOGIN_READ_REQUEST_SET.contains(sendReq.getUseType())) {
                sendCodeService.sendEmailWithUseType(sendReq.getEmail(), sendReq.getUseType());
            } else if (AuthCodeUseTypeEnum.EMAIL_NEED_LOGIN_READ_DB_SET.contains(sendReq.getUseType())) {
                // 需要登陆，但是手机号从DB里读
                UserDto userDto = userService.getUserByUid(loginUser.getUserId());
                sendCodeService.sendEmailWithUseType(userDto.getEmail(), sendReq.getUseType());
            } else {
                // 没有被录入的类型，直接抛错误，后面再加
                throw new SendAuthCodeException(UcErrorCode.ERR_502_ILLEGAL_ARGUMENTS);
            }
        }

        return GenericDto.success(null);
    }

    @GetMapping("login/check")
    public GenericDto<Boolean> getLoginState(HttpServletRequest request) {
        String token = WebUtil.getTokenFromRequest(request);
        if (StringUtils.isNotBlank(token)) {
            LoginUser loginUser = cacheService.getUserByToken(token);
            if (loginUser != null && loginUser.getExpireAt() > System.currentTimeMillis()) {
                return GenericDto.success(true);
            }
        }
        return GenericDto.success(false);
    }

    @PostMapping("/register-code/verify")
    public GenericDto<Object> verifyRegisterCode(@RequestBody RegisterCodeVerifyReq verifyReq) {
        if (StringUtils.isBlank(verifyReq.getEmail())
                && StringUtils.isBlank(verifyReq.getPhone())) {
            throw new LoginException(UcErrorCode.ERR_10026_PHONE_EMAIL_CANNOT_BOTH_EMPTY);
        }
        if (StringUtils.isNotBlank(verifyReq.getEmail())
                && StringUtils.isNotBlank(verifyReq.getPhone())) {
            throw new LoginException(UcErrorCode.ERR_10027_CHOOSE_PHONE_OR_EMAIL);
        }

        ClientAuthTypeEnum authTypeEnum = StringUtils.isNotBlank(verifyReq.getPhone())
                ? ClientAuthTypeEnum.PHONE : ClientAuthTypeEnum.EMAIL;

        String accountName =
                ClientAuthTypeEnum.getAccountNameByAuthType(verifyReq.getPhone(), verifyReq.getEmail(), authTypeEnum);

        AuthCode authCode =
                cacheService.getAuthCode(accountName,
                        AuthCodeUseTypeEnum.REGISTER,
                        authTypeEnum);
        if (authCode == null
                || authCode.getCode() == null
                || !authCode.getCode().equals(verifyReq.getAuthCode())) {
            throw new LoginException(UcErrorCode.ERR_10032_WRONG_SMS_CODE);
        }
        return GenericDto.success(null);
    }
}