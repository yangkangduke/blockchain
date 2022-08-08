package com.seeds.uc.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.seeds.uc.dto.UserDto;
import com.seeds.uc.dto.redis.LoginUserDTO;
import com.seeds.uc.dto.request.*;
import com.seeds.uc.dto.response.LoginResp;
import com.seeds.uc.dto.response.ProfileResp;
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
     * metamask登陆
     */
    LoginResp metamaskLogin(MetaMaskReq metaMaskReq);

    void bindMetamask(MetaMaskReq metaMaskReq, Long uId);


    /**
     * 忘记密码-验证链接
     *
     * @param encode
     */
    void forgotPasswordVerifyLink(String encode, String account);

    /**
     * 忘记密码-修改密码
     *
     * @param resetPasswordReq
     */
    void forgotPasswordReset(ResetPasswordReq resetPasswordReq);

    /**
     * 2fa校验
     * @param loginReq
     * @return
     */
    LoginResp twoFactorCheck(TwoFactorLoginReq loginReq);

    /**
     * 校验登陆
     * @param loginReq
     */
    UserDto verifyLogin(LoginReq loginReq);

    /**
     * 获取用户信息
     * @param loginUser
     * @return
     */
    ProfileResp getInfo(LoginUserDTO loginUser);

    /**
     * 修改昵称
     * @param nickname
     * @param loginUser
     */
    Boolean updateNickname(String nickname, LoginUserDTO loginUser);

    /**
     * 修改密码
     * @param userId
     * @param password
     * @return
     */
    Boolean updatePassword(Long userId, String password);

    /**
     * 修改邮箱
     * @param email
     * @param loginUser
     * @return
     */
    Boolean updateEmail(String email, LoginUserDTO loginUser);

    /**
     * 删除ga
     * @param ucUser
     */
    void deleteGa(UcUser ucUser);

    /**
     * 验证是否有ga
     * @param userId
     * @return
     */
    Boolean verifyGa(Long userId);
}
