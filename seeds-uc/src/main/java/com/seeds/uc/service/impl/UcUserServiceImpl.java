package com.seeds.uc.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seeds.uc.dto.AuthCode;
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
import com.seeds.uc.util.PasswordUtil;
import com.seeds.uc.util.RandomUtil;
import com.seeds.uc.util.WebUtil;
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
    private ISendCodeService sendCodeService;
    @Autowired
    private IUcSecurityStrategyService iUcSecurityStrategyService;
    @Autowired
    private RedissonClient redissonClient;

    /**
     * 发送验证码
     *
     * @param sendReq
     */
    @Override
    public String sendCode(SendCodeReq sendReq) {
        AuthCodeUseTypeEnum useType = sendReq.getUseType();
        if (AuthCodeUseTypeEnum.BIND_EMAIL.equals(useType) ) {
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
    public Boolean bindEmail(BindEmailReq verifyReq, HttpServletRequest request) {
        String email = verifyReq.getEmail();
        String code = verifyReq.getCode();
        AuthCode authCode = cacheService.getAuthCode(email, verifyReq.getUseType(), ClientAuthTypeEnum.EMAIL);
        if (authCode == null || authCode.getCode() == null || !authCode.getCode().equals(code)) {
            throw new InvalidArgumentsException(UcErrorCodeEnum.ERR_10033_WRONG_EMAIL_CODE);
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
    public Boolean verifyAccount(String account) {
        UcUser ucUser = this.getOne(new QueryWrapper<UcUser>().lambda()
                .eq(UcUser::getAccount, account));
        if (ucUser != null) {
            throw new InvalidArgumentsException(UcErrorCodeEnum.ERR_10061_EMAIL_ALREADY_BEEN_USED);
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
    public LoginResp registerAccount(RegisterReq registerReq, HttpServletRequest request) {
        LoginUser loginUser = null;
        // 获取当前登陆人信息
        String loginToken = WebUtil.getTokenFromRequest(request);
        if (loginToken != null) {
            loginUser = cacheService.getUserByToken(loginToken);
        }
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
     * @param accountLoginReq
     * @return
     */
    @Override
    public LoginResp loginAccount(AccountLoginReq accountLoginReq) {
        String account = accountLoginReq.getAccount();
        UcUser ucUser = this.getOne(new QueryWrapper<UcUser>().lambda()
                .eq(UcUser::getAccount, account));
        if (ucUser == null) {
            throw new InvalidArgumentsException("Account does not exist");
        }
        String loginPassword = PasswordUtil.getPassword(accountLoginReq.getPassword(), ucUser.getSalt());
        if (!loginPassword.equals(ucUser.getPassword())) {
            throw new InvalidArgumentsException(UcErrorCodeEnum.ERR_10013_ACCOUNT_NAME_PASSWORD_INCORRECT);
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
     * @param request
     * @return
     */
    @Override
    public LoginResp loginMetaMask(MetaMaskLoginReq loginReq, HttpServletRequest request) {
        String publicAddress = loginReq.getPublicAddress();
        String message = loginReq.getMessage();
        String signature = loginReq.getSignature();
        // 地址合法性校验
        if (!WalletUtils.isValidAddress(publicAddress)) {
            // 不合法直接返回错误
            throw new InvalidArgumentsException("Illegal address format");
        }
        // 校验签名信息
        if (!CryptoUtils.validate(signature, message, publicAddress)) {
            throw new InvalidArgumentsException("Signature verification failed");
        }
        UcUser one = this.getOne(new QueryWrapper<UcUser>().lambda()
                .eq(UcUser::getSalt, publicAddress));
        if (one == null) {
            throw new InvalidArgumentsException("User does not exist");
        }
        // 下发uc token
        String ucToken = RandomUtil.genRandomToken(one.getId().toString());
        // 将产生的uc token存入redis
        cacheService.putUserWithTokenAndLoginName(ucToken, one.getId(), publicAddress);

        // 修改nonce
        this.metamaskNonce(publicAddress, request);

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
    public String metamaskNonce(String publicAddress, HttpServletRequest request) {
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
