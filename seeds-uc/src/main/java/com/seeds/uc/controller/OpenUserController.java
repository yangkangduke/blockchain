package com.seeds.uc.controller;


import com.seeds.common.dto.GenericDto;
import com.seeds.uc.dto.redis.LoginUserDTO;
import com.seeds.uc.dto.request.ChangePasswordReq;
import com.seeds.uc.dto.request.NickNameReq;
import com.seeds.uc.dto.response.UserInfoResp;
import com.seeds.uc.enums.AuthCodeUseTypeEnum;
import com.seeds.uc.enums.ClientAuthTypeEnum;
import com.seeds.uc.enums.UcErrorCodeEnum;
import com.seeds.uc.exceptions.InvalidArgumentsException;
import com.seeds.uc.exceptions.LoginException;
import com.seeds.uc.exceptions.PasswordException;
import com.seeds.uc.model.UcUser;
import com.seeds.uc.service.IGoogleAuthService;
import com.seeds.uc.service.IUcUserService;
import com.seeds.uc.service.SendCodeService;
import com.seeds.uc.service.impl.CacheService;
import com.seeds.uc.util.PasswordUtil;
import com.seeds.uc.util.WebUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * <p>
 * user table 前端控制器
 * </p>
 *
 * @author yk
 * @since 2022-07-09
 */
@RestController
@Api(tags = "用户")
public class OpenUserController {

    @Autowired
    private IUcUserService ucUserService;
    @Autowired
    private IGoogleAuthService googleAuthService;
    @Autowired
    private CacheService cacheService;
    @Autowired
    private SendCodeService sendCodeService;


    @GetMapping("/info")
    @ApiOperation(value = "获取用户信息", notes = "获取用户信息")
    public GenericDto<UserInfoResp> getInfo(HttpServletRequest request) {
        // 获取当前登陆人信息
        String loginToken = WebUtil.getTokenFromRequest(request);
        LoginUserDTO loginUser = cacheService.getUserByToken(loginToken);
        return GenericDto.success(ucUserService.getInfo(loginUser));
    }

    @PostMapping("/logout")
    @ApiOperation(value = "退出登陆", notes = "退出登陆")
    public GenericDto<Object> logout(HttpServletRequest request) {
        String token = WebUtil.getTokenFromRequest(request);

        cacheService.removeUserByToken(token);
        return GenericDto.success(null);
    }

    @PutMapping("/change/nickname")
    @ApiOperation(value = "修改昵称", notes = "修改昵称")
    public GenericDto<Object> updateNickname(@Valid @RequestBody NickNameReq nickNameReq, HttpServletRequest request) {
        // 获取当前登陆人信息
        String loginToken = WebUtil.getTokenFromRequest(request);
        LoginUserDTO loginUser = cacheService.getUserByToken(loginToken);
        return GenericDto.success(ucUserService.updateNickname(nickNameReq.getNickname(), loginUser));
    }


    @PutMapping("/change/password")
    @ApiOperation(value = "修改密码", notes = "修改密码")
    public GenericDto<Object> updatePassword(@Valid @RequestBody ChangePasswordReq changePasswordReq, HttpServletRequest request) {
        String password = changePasswordReq.getPassword();
        String oldPassword = changePasswordReq.getOldPassword();
        ClientAuthTypeEnum authTypeEnum = changePasswordReq.getAuthTypeEnum();
        // 获取当前登陆人信息
        String loginToken = WebUtil.getTokenFromRequest(request);
        LoginUserDTO loginUser = cacheService.getUserByToken(loginToken);
        UcUser user = ucUserService.getById(loginUser.getUserId());
        // 判断原密码是否正确
        if (!user.getPassword().equals(PasswordUtil.getPassword(oldPassword, user.getSalt()))) {
            throw new PasswordException(UcErrorCodeEnum.ERR_10043_WRONG_OLD_PASSWORD);
        }

        // 判断code是否正确
        if (authTypeEnum.equals(ClientAuthTypeEnum.EMAIL)) {
            sendCodeService.verifyEmailWithUseType(user.getEmail(), changePasswordReq.getCode(), AuthCodeUseTypeEnum.CHANGE_PASSWORD);

        } else if (authTypeEnum.equals(ClientAuthTypeEnum.GA)) {
            if (!googleAuthService.verify(changePasswordReq.getCode(), user.getGaSecret())) {
                throw new LoginException(UcErrorCodeEnum.ERR_10088_WRONG_GOOGLE_AUTHENTICATOR_CODE);
            }
        } else {
            throw new InvalidArgumentsException(UcErrorCodeEnum.ERR_504_MISSING_ARGUMENTS);
        }
        // 修改密码
        return GenericDto.success(ucUserService.updatePassword(user.getId(), password));
    }


}
