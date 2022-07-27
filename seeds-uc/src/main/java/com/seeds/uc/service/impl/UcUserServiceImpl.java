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
import com.seeds.uc.dto.AuthCode;
import com.seeds.uc.dto.ForgotPasswordCode;
import com.seeds.uc.dto.LoginUser;
import com.seeds.uc.dto.request.*;
import com.seeds.uc.dto.response.LoginResp;
import com.seeds.uc.enums.*;
import com.seeds.uc.exceptions.InvalidArgumentsException;
import com.seeds.uc.mapper.UcUserMapper;
import com.seeds.uc.model.UcSecurityStrategy;
import com.seeds.uc.model.UcUser;
import com.seeds.uc.service.ISendCodeService;
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

    public static final String EMAIL_CONTENT = "Reset password address, it will expire in 10 minutes：";
    @Autowired
    private CacheService cacheService;
    @Autowired
    private ResetPasswordProperties resetPasswordProperties;
    @Autowired
    private ISendCodeService sendCodeService;
    @Autowired
    private IUcSecurityStrategyService iUcSecurityStrategyService;
    @Autowired
    private EmailProperties emailProperties;

    /**
     * 发送验证码
     *
     * @param sendReq
     */
    @Override
    public String sendCode(SendCodeReq sendReq) {
        AuthCodeUseTypeEnum useType = sendReq.getUseType();
        if (AuthCodeUseTypeEnum.BIND_EMAIL.equals(useType)) {
            return sendCodeService.sendEmailWithUseType(sendReq.getAddress(), sendReq.getUseType());
        } else {
            throw new InvalidArgumentsException("incorrect type");
        }

    }

    /**
     * 邮箱验证码校验
     *
     * @param verifyReq
     */
    @Override
    public void bindEmail(BindEmailReq verifyReq, LoginUser loginUser) {
        Long userId = loginUser.getUserId();
        long createTime = System.currentTimeMillis();
        String email = verifyReq.getEmail();
        String code = verifyReq.getCode();
        AuthCode authCode = cacheService.getAuthCode(email, verifyReq.getUseType(), ClientAuthTypeEnum.EMAIL);
        if (authCode == null || authCode.getCode() == null || !authCode.getCode().equals(code)) {
            throw new InvalidArgumentsException(UcErrorCodeEnum.ERR_10033_WRONG_EMAIL_CODE);
        }

        // 修改邮箱信息到user表
        this.updateById(UcUser.builder()
                .email(email)
                .id(userId)
                .build());

        iUcSecurityStrategyService.saveOrUpdate(UcSecurityStrategy.builder()
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
    public void verifyAccount(String account) {
        UcUser ucUser = this.getOne(new QueryWrapper<UcUser>().lambda()
                .eq(UcUser::getAccount, account));

        if (ucUser != null) {
            throw new InvalidArgumentsException(UcErrorCodeEnum.ERR_10061_EMAIL_ALREADY_BEEN_USED);
        }
    }

    /**
     * 注册用户
     *
     * @param registerReq
     * @return
     */
    @Override
    public LoginResp registerAccount(RegisterReq registerReq, LoginUser loginUser) {
        // loginUser不是null说明当前登陆的是metamsk,还要绑定metamsk
        String account = registerReq.getAccount();
        this.verifyAccount(account);
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

            this.saveOrUpdate(ucUser, new QueryWrapper<UcUser>().lambda()
                    .eq(UcUser::getId, loginUser.getUserId()));
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
     * @param accountLoginReq
     * @return
     */
    @Override
    public LoginResp loginAccount(AccountLoginReq accountLoginReq) {
        String account = accountLoginReq.getAccount();
        String password = accountLoginReq.getPassword();
        UcUser ucUser = this.getOne(new QueryWrapper<UcUser>().lambda()
                .eq(UcUser::getAccount, account));
        if (ucUser == null) {
            throw new InvalidArgumentsException(UcErrorCodeEnum.ERR_10001_ACCOUNT_YET_NOT_REGISTERED);
        }
        String loginPassword = PasswordUtil.getPassword(password, ucUser.getSalt());
        if (!loginPassword.equals(ucUser.getPassword())) {
            throw new InvalidArgumentsException(UcErrorCodeEnum.ERR_10013_ACCOUNT_NAME_PASSWORD_INCORRECT);
        }
        // 下发uc token
        Long id = ucUser.getId();
        String ucToken = RandomUtil.genRandomToken(id.toString());
        // 将产生的uc token存入redis
        cacheService.putUserWithTokenAndLoginName(ucToken, id, account);

        return LoginResp.builder()
                .ucToken(ucToken)
                .build();
    }

    /**
     * metamask登陆
     *
     * @return
     */
    @Override
    public LoginResp loginMetaMask(MetaMaskLoginReq loginReq) {
        String publicAddress = loginReq.getPublicAddress();
        String message = loginReq.getMessage();
        String signature = loginReq.getSignature();
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
        if (one == null) {
            throw new InvalidArgumentsException(UcErrorCodeEnum.ERR_10001_ACCOUNT_YET_NOT_REGISTERED);
        }
        // 下发uc token
        String ucToken = RandomUtil.genRandomToken(one.getId().toString());
        // 将产生的uc token存入redis
        cacheService.putUserWithTokenAndLoginName(ucToken, one.getId(), publicAddress);

        // 修改user信息
        long time = System.currentTimeMillis();
        String nonce = RandomUtil.getRandomSalt();
        UcUser ucUser = UcUser.builder()
                .updatedAt(time)
                .nonce(nonce)
                .metamaskFlag(1)
                .build();
        this.update(ucUser, new QueryWrapper<UcUser>().lambda()
                .eq(UcUser::getPublicAddress, publicAddress));

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
    public String metamaskNonce(String publicAddress, LoginUser loginUser) {
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

    /**
     * 忘记密码-发送邮件
     *
     * @param forgotPasswordReq
     */
    @Override
    public void forgotPasswordSeedEmail(ForgotPasswordReq forgotPasswordReq) {
        String account = forgotPasswordReq.getAccount();
        String email = account;
        // 判断账号是否存在
        UcUser one = this.getOne(new QueryWrapper<UcUser>().lambda()
                .eq(UcUser::getAccount, account));
        if (one == null) {
            throw new InvalidArgumentsException(UcErrorCodeEnum.ERR_15000_ACCOUNT_NOT);
        }
        // 发送加密邮件 10分钟过期
        long endTimes = System.currentTimeMillis() + 600 * 1000;
        // 先加密，再url转码,顺序不能修改
        String encode = Base64.encode(DigestUtil.Encrypt(account + ";" + email));
        MailAccount mailAccount = new MailAccount();
        mailAccount.setHost(emailProperties.getHost());
        mailAccount.setPort(25);
        mailAccount.setAuth(true);
        mailAccount.setFrom(emailProperties.getFrom());
        mailAccount.setUser(emailProperties.getUser());
        mailAccount.setPass(emailProperties.getPass());
        MailUtil.send(mailAccount, CollUtil.newArrayList(account), "forgot password", EMAIL_CONTENT + "<a>" + resetPasswordProperties.getUrl() + encode + "</a>", true);
        cacheService.putForgotPasswordCode(account, email, endTimes);
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
                String[] split = decrypt.split(";");
                String account = split[0];
                String email = split[1];
                long curtime = System.currentTimeMillis();
                ForgotPasswordCode forgotPasswordCode = cacheService.getForgotPasswordCode(account);
                if (forgotPasswordCode == null) {
                    throw new InvalidArgumentsException(UcErrorCodeEnum.ERR_15001_ACCOUNT_VERIFICATION_FAILED);
                }
                if (forgotPasswordCode.getExpireAt() <= curtime) {
                    throw new InvalidArgumentsException(UcErrorCodeEnum.ERR_15001_ACCOUNT_VERIFICATION_FAILED);
                }
                if (!forgotPasswordCode.getEmail().equals(email)) {
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
        this.update(UcUser.builder()
                .password(password)
                .salt(salt)
                .build(), new QueryWrapper<UcUser>().lambda()
                .eq(UcUser::getAccount, account));
    }


}
