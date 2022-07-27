package com.seeds.uc.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.seeds.uc.dto.LoginUser;
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
    String sendCode(SendCodeReq sendReq);

    /**
     * 邮箱验证码校验
     *
     * @param verifyReq
     */
    void bindEmail(BindEmailReq verifyReq, LoginUser loginUser);

    /**
     * 账号重复性校验
 L   *
     * @param account
     * @return
     */
    void verifyAccount(String account);

    /**
     * 注册用户
     *
     * @param registerReq
     * @return
     */
    LoginResp registerAccount(RegisterReq registerReq, LoginUser loginUser);

    /**
     * 登陆
     * @param accountLoginReq
     * @return
     */
    LoginResp loginAccount(AccountLoginReq accountLoginReq);

    /**
     * metamask登陆
     */
    LoginResp loginMetaMask(MetaMaskLoginReq loginReq);

    /**
     * metamask登陆获取随机数
     * @return
     */
    String metamaskNonce(String publicAddress, LoginUser loginUser);


}
