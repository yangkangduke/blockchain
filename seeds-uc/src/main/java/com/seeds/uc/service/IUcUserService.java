package com.seeds.uc.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.seeds.uc.dto.request.*;
import com.seeds.uc.dto.response.LoginResp;
import com.seeds.uc.model.UcUser;

import javax.servlet.http.HttpServletRequest;

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
    void sendCode(SendCodeReq sendReq);

    /**
     * 邮箱验证码校验
     *
     * @param verifyReq
     */
    Boolean bindEmail(BindEmailReq verifyReq, HttpServletRequest request);

    /**
     * 账号重复性校验
     *
     * @param account
     * @return
     */
    Boolean verifyAccount(String account);

    /**
     * 注册用户
     *
     * @param registerReq
     * @return
     */
    LoginResp registerAccount(RegisterReq registerReq, HttpServletRequest request);

    /**
     * 登陆
     * @param accountLoginReq
     * @return
     */
    LoginResp loginAccount(AccountLoginReq accountLoginReq);

    /**
     * metamask登陆
     * @param request
     */
    LoginResp loginMetaMask(MetaMaskLoginReq loginReq, HttpServletRequest request);

    /**
     * metamask登陆获取随机数
     * @return
     */
    String metamaskNonce(String publicAddress, HttpServletRequest request);


}
