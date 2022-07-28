package com.seeds.uc.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.seeds.uc.dto.redis.LoginUserDTO;
import com.seeds.uc.dto.request.*;
import com.seeds.uc.dto.response.LoginResp;
import com.seeds.uc.model.UcUser;

/**
 * <p>
 * user table 服务类
 * </p>
 *
 * @author yk
 * @since 2022-07-09
 */
public interface IUcUserService extends IService<UcUser> {

    /**
     * 发送验证码
     *
     * @param sendReq
     */
    String bindEmailSend(SendCodeReq sendReq);

    /**
     * 邮箱验证码校验
     *
     * @param verifyReq
     */
    void bindEmail(BindEmailReq verifyReq, LoginUserDTO loginUser);

    /**
     * 账号重复性校验
     * L   *
     *
     * @param account
     * @return
     */
    void verifyAccount(String account);

    /**
     * 注册邮箱账号
     *
     * @param registerReq
     * @return
     */
    LoginResp registerEmailAccount(RegisterReq registerReq);

    /**
     * 账号登陆
     *
     * @param accountLoginReq
     * @return
     */
    LoginResp login(LoginReq accountLoginReq);

    /**
     * metamask验证
     */
    LoginResp metamaskVerify(MetaMaskReq metaMaskReq);

    /**
     * metamask登陆获取随机数
     *
     * @return
     */
    String metamaskNonce(MetaMaskReq metaMaskReq);

    /**
     * 忘记密码-发送邮件
     *
     * @param forgotPasswordReq
     */
    void forgotPasswordSeedEmail(ForgotPasswordReq forgotPasswordReq);

    /**
     * 忘记密码-验证链接
     *
     * @param encode
     */
    void forgotPasswordVerifyLink(String encode);

    /**
     * 忘记密码-修改密码
     *
     * @param changePasswordReq
     */
    void forgotPasswordChangePassword(ChangePasswordReq changePasswordReq);

    /**
     * 注册邮箱账号-发送邮箱验证码
     * @param email
     */
    void registerEmailSend(String email);

    LoginResp twoFactorCheck(TwoFactorLoginReq loginReq);
}
