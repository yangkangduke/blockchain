package com.seeds.uc.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.seeds.admin.enums.WhetherEnum;
import com.seeds.common.dto.GenericDto;
import com.seeds.uc.dto.UserDto;
import com.seeds.uc.dto.redis.AuthCodeDTO;
import com.seeds.uc.dto.redis.LoginUserDTO;
import com.seeds.uc.dto.request.*;
import com.seeds.uc.dto.response.LoginResp;
import com.seeds.uc.dto.response.MetamaskAuthResp;
import com.seeds.uc.enums.AuthCodeUseTypeEnum;
import com.seeds.uc.enums.ClientAuthTypeEnum;
import com.seeds.uc.enums.UcErrorCodeEnum;
import com.seeds.uc.exceptions.InvalidArgumentsException;
import com.seeds.uc.exceptions.SendAuthCodeException;
import com.seeds.uc.model.UcUser;
import com.seeds.uc.service.IUcUserService;
import com.seeds.uc.service.IUsUserLoginLogService;
import com.seeds.uc.service.SendCodeService;
import com.seeds.uc.service.impl.CacheService;
import com.seeds.uc.util.RandomUtil;
import com.seeds.uc.util.WebUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@Api(tags = "auth")
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private IUcUserService ucUserService;
    @Autowired
    private SendCodeService sendCodeService;
    //@Autowired
    //private LoginProperties loginProperties;
    @Autowired
    private CacheService cacheService;
    @Autowired
    private IUsUserLoginLogService userLoginLogService;
    @Autowired
    private KafkaTemplate kafkaTemplate;


    @PostMapping("/register/email")
    @ApiOperation(value = "注册邮箱账号",
            notes = "1.调用/auth/email/send 发送邮箱验证码 参数authType=REGISTER " +
                    "2.调用/auth/register/email 注册邮箱账号 ")
    public GenericDto<LoginResp> registerEmailAccount(@Valid @RequestBody RegisterReq registerReq) {
        return GenericDto.success(ucUserService.registerEmail(registerReq));
    }

    @ApiOperation(value = "校验邀请码", notes = "校验邀请码")
    @PostMapping("/register/validate-invite-code")
    public GenericDto<Object> validateInviteCode(@Valid @RequestBody InviteCodeReq req) {
        ucUserService.registerWriteOffsInviteCode(req.getInviteCode(), req.getEmail(), WhetherEnum.NO.value());
        return GenericDto.success(null);
    }

    @GetMapping("/register/check-email")
    @ApiOperation(value = "校验邮箱", notes = "校验邮箱")
    public GenericDto<Boolean> registerCheckEmail(@Valid @NotNull String email) {
        return GenericDto.success(ucUserService.registerCheckEmail(email));
    }


    @PostMapping("/login")
    @ApiOperation(value = "账号登陆", notes = "1.调用/auth/login接口，返回token和authType " +
            "2.调用/auth/2fa/login，参数authCode填的值根据上一个接口返回的authType来决定，如果是2就填email的验证码，如果是3就填ga的验证码， 返回的ucToken就是登陆成功的凭证 ")
    public GenericDto<LoginResp> login(@Valid @RequestBody LoginReq loginReq) {
        // 校验是否需要2FA认证
        //获取用户真实ip地址
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String clientIp = WebUtil.getIpAddr(request);
        // 不需要进行2FA
        if (userLoginLogService.checkNeed2FA(loginReq.getEmail(), clientIp)) {
            // 校验账号、密码
            UserDto userDto = ucUserService.verifyLogin(loginReq);
            LoginResp login = ucUserService.buildLoginResponse(userDto.getUid(), userDto.getEmail());
            // 生产登陆成功消息
            try {
                Map loginMap = new HashMap<>();
                loginMap.put("email",loginReq.getEmail());
                loginMap.put("userIp",loginReq.getUserIp());
                loginMap.put("serviceRegion",loginReq.getServiceRegion());
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
            return GenericDto.success(login);
        }

        LoginResp login = ucUserService.login(loginReq);
        if (login.getUcToken() == null) {
            return GenericDto.failure(UcErrorCodeEnum.ERR_10070_PLEASE_ENTER_2FA.getDescEn(), UcErrorCodeEnum.ERR_10070_PLEASE_ENTER_2FA.getCode(), login);
        }
        return GenericDto.success(login);
    }

    @PostMapping("/2fa/login")
    @ApiOperation(value = "2fa登陆", notes = "1.调用/auth/login接口，返回token和authType " +
            "2.调用/auth/2fa/login，参数authCode填的值根据上一个接口返回的authType来决定，如果是2就填email的验证码，如果是3就填ga的验证码， 返回的ucToken就是登陆成功的凭证 ")
    public GenericDto<LoginResp> twoFactorCheck(@Valid @RequestBody TwoFactorLoginReq loginReq) {
        return GenericDto.success(ucUserService.twoFactorCheck(loginReq));

    }

    @ApiOperation(value = "生成metamask的nonce", notes = "生成metamask的nonce")
    @PostMapping("/metamask/generate-nonce")
    public GenericDto<MetamaskAuthResp> generateNonce(@Valid @RequestBody MetamaskVerifyReq metamaskVerifyReq) {
        String nonce = RandomUtil.getRandomSalt();
        cacheService.putGenerateMetamaskAuth(metamaskVerifyReq.getPublicAddress(), nonce);
        return GenericDto.success(
                MetamaskAuthResp.builder()
                        .nonce(nonce)
                        .publicAddress(metamaskVerifyReq.getPublicAddress())
                        .build());
    }

    @PostMapping("/metamask/login")
    @ApiOperation(value = "metamask登陆",
            notes = "1.调用/auth/metamask/generateNonce生成nonce " +
                    "2.前端根据nonce生成签名信息 " +
                    "3.调用/auth/metamask/login登陆验证签名信息，验证成功返回ucToken ")
    public GenericDto<LoginResp> metamaskLogin(@Valid @RequestBody MetamaskVerifyReq metamaskVerifyReq) {
        return GenericDto.success(ucUserService.metamaskLogin(metamaskVerifyReq));
    }

    @GetMapping("/invite-flag")
    @ApiOperation(value = "是否需要邀请码")
    public GenericDto<Boolean> inviteFlag(@RequestParam(required = false) String account) {
        return GenericDto.success(ucUserService.inviteFlag(account));
    }

    @PostMapping("/forgot-password/reset")
    @ApiOperation(value = "忘记密码-重置密码", notes = "1.调用/auth/email/send 参数authType=RESET_PASSWORD获取邮箱验证码 " +
            "2.调用/auth/forgot-password/reset 重置邮箱账号的密码 ")
    public GenericDto<LoginResp> forgotPasswordReset(@Valid @RequestBody ResetPasswordReq resetPasswordReq) {
        String code = resetPasswordReq.getCode();
        String email = resetPasswordReq.getEmail();
        ucUserService.forgotPasswordVerify(code, email);
        return GenericDto.success(ucUserService.forgotPasswordReset(resetPasswordReq));
    }


    @PostMapping("/reset/ga")
    @ApiOperation(value = "重置GA", notes = "1.调用/auth/email/send 参数authType=RESET_GA获取邮箱验证码 " +
            "2.调用/auth/reset/ga 重置GA ")
    public GenericDto<Object> resetGa(@Valid @RequestBody ResetGaReq resetGaReq) {
        String email = resetGaReq.getEmail();
        String code = resetGaReq.getCode();
        // 验证邮箱和验证码是否正确
        AuthCodeDTO authCode = cacheService.getAuthCode(email, AuthCodeUseTypeEnum.RESET_GA, ClientAuthTypeEnum.EMAIL);
        if (authCode == null || !code.equals(authCode.getCode())) {
            throw new InvalidArgumentsException(UcErrorCodeEnum.ERR_17000_EMAIL_VERIFICATION_FAILED);
        }
        // 查看email是否存在
        UcUser one = ucUserService.getOne(new LambdaQueryWrapper<UcUser>().eq(UcUser::getEmail, email));
        if (one == null) {
            throw new InvalidArgumentsException(UcErrorCodeEnum.ERR_10001_ACCOUNT_YET_NOT_REGISTERED);
        }
        // 删除已经绑定的ga
        ucUserService.deleteGa(one);
        return GenericDto.success(null);
    }

    @ApiOperation(value = "发送邮件", notes = "如果没有收到邮件，验证码默认是123456")
    @PostMapping("/email/send")
    public GenericDto<Object> sendEmailCode(@Valid @RequestBody AuthCodeSendReq sendReq, HttpServletRequest request) {
        log.info("AuthController - sendEmailCode got request: {}", sendReq);
        if (AuthCodeUseTypeEnum.CODE_NO_NEED_LOGIN_READ_REQUEST.contains(sendReq.getUseType())) {
            // 不需要登陆，请求里带邮箱，如: REGISTER
            sendCodeService.sendEmailWithUseType(sendReq.getEmail(), sendReq.getUseType());
        } else if (AuthCodeUseTypeEnum.CODE_NO_NEED_LOGIN_READ_DB.contains(sendReq.getUseType())) {
            // 需要token(拒绝登陆后发的用于2FA的token)，邮箱从数据库查, 如：LOGIN
            sendCodeService.sendEmailWithTokenAndUseType(sendReq.getToken(), sendReq.getUseType());
        } else {
            // 需要登陆的，从db中读
            String loginToken = WebUtil.getTokenFromRequest(request);
            LoginUserDTO loginUser = cacheService.getUserByToken(loginToken);
            if (StringUtils.isBlank(loginToken) || loginUser == null) {
                throw new SendAuthCodeException(UcErrorCodeEnum.ERR_401_NOT_LOGGED_IN);
            }
            UcUser user = ucUserService.getById(loginUser.getUserId());
            // 绑定邮箱、修改邮箱
            if (AuthCodeUseTypeEnum.EMAIL_NEED_LOGIN_READ_REQUEST_SET.contains(sendReq.getUseType())) {
                sendCodeService.sendEmailWithUseType(sendReq.getEmail(), sendReq.getUseType());
                // 绑定ga、修改密码、绑定metamask
            } else if (AuthCodeUseTypeEnum.EMAIL_NEED_LOGIN_READ_DB_SET.contains(sendReq.getUseType())) {
                sendCodeService.sendEmailWithUseType(user.getEmail(), sendReq.getUseType());
            } else {
                throw new SendAuthCodeException(UcErrorCodeEnum.ERR_502_ILLEGAL_ARGUMENTS);
            }

        }
        return GenericDto.success(null);
    }

    @GetMapping("/refreshToken/{refreshToken}")
    @ApiOperation(value = "刷新ucToken")
    public GenericDto<String> refreshUcToken(@PathVariable("refreshToken") String refreshToken) {
        LoginUserDTO loginUser = cacheService.getUserByRefreshToken(refreshToken);
        if (loginUser == null) {
            return GenericDto.failure(UcErrorCodeEnum.ERR_10023_TOKEN_EXPIRED.getDescEn(), UcErrorCodeEnum.ERR_10023_TOKEN_EXPIRED.getCode(), null);
        }
        Long userId = loginUser.getUserId();
        // 生成uc token给用户
        String ucToken = RandomUtil.genRandomToken(userId.toString());
        // 将token存入redis，用户进入登陆态
        cacheService.putUserWithTokenAndLoginName(ucToken, userId, loginUser.getLoginName());
        return GenericDto.success(ucToken);
    }
}
