package com.seeds.uc.web.user.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.seeds.uc.model.send.dto.request.EmailCodeSendReq;
import com.seeds.uc.model.send.dto.request.BndEmailReq;
import com.seeds.uc.model.user.dto.request.LoginReq;
import com.seeds.uc.model.user.dto.request.RegisterReq;
import com.seeds.uc.model.user.dto.response.LoginResp;
import com.seeds.uc.model.user.entity.UcUser;

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
     * 发送邮箱验证码
     *
     * @param sendReq
     */
    Boolean sendEmailCode(EmailCodeSendReq sendReq);

    /**
     * 邮箱验证码校验
     *
     * @param verifyReq
     */
    Boolean bindEmail(BndEmailReq verifyReq, HttpServletRequest request);

    /**
     * 账号重复性校验
     *
     * @param account
     * @return
     */
    Boolean accountVerify(String account);

    /**
     * 注册用户
     *
     * @param registerReq
     * @return
     */
    LoginResp createAccount(RegisterReq registerReq);

    /**
     * 登陆
     * @param loginReq
     * @return
     */
    LoginResp login(LoginReq loginReq);
}
