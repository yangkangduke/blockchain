package com.seeds.uc.controller;


import com.seeds.admin.dto.response.ProfileInfoResp;
import com.seeds.common.dto.GenericDto;
import com.seeds.common.web.context.UserContext;
import com.seeds.uc.dto.redis.LoginUserDTO;
import com.seeds.uc.dto.request.*;
import com.seeds.uc.dto.request.security.item.ShareLinkReq;
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
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class OpenUserController {

    @Autowired
    private IUcUserService ucUserService;
    @Autowired
    private IGoogleAuthService googleAuthService;
    @Autowired
    private CacheService cacheService;
    @Autowired
    private SendCodeService sendCodeService;


    @GetMapping("/getInfo")
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

    @PutMapping("/change/introduction")
    @ApiOperation(value = "修改简介", notes = "修改简介")
    public GenericDto<Object> updateIntroduction(@Valid @RequestBody IntroductionReq introductionReq, HttpServletRequest request) {
        // 获取当前登陆人信息
        String loginToken = WebUtil.getTokenFromRequest(request);
        LoginUserDTO loginUser = cacheService.getUserByToken(loginToken);
        long currentTime = System.currentTimeMillis();
        return GenericDto.success(ucUserService.updateById(UcUser.builder()
                .id(loginUser.getUserId())
                .introduction(introductionReq.getIntroduction())
                .updatedAt(currentTime)
                .build()));
    }


    @PutMapping("/change/ShareLink")
    @ApiOperation(value = "修改社交链接", notes = "修改社交链接")
    public GenericDto<Object> updateShareLink(@Valid @RequestBody ShareLinkReq shareLinkReq, HttpServletRequest request) {
        // 获取当前登陆人信息
        String loginToken = WebUtil.getTokenFromRequest(request);
        LoginUserDTO loginUser = cacheService.getUserByToken(loginToken);
        long currentTime = System.currentTimeMillis();
        return GenericDto.success(ucUserService.updateById(UcUser.builder()
                .id(loginUser.getUserId())
                .facebook(shareLinkReq.getFacebook())
                .twitter(shareLinkReq.getTwitter())
                .instagram(shareLinkReq.getInstagram())
                .updatedAt(currentTime)
                .build()));
    }

    @PutMapping("/change/avatar")
    @ApiOperation(value = "修改头像", notes = "修改头像")
    public GenericDto<Object> updateAvatar(@Valid @RequestBody AvatarReq avatarReq, HttpServletRequest request) throws Exception {
        // 获取当前登陆人信息
        String loginToken = WebUtil.getTokenFromRequest(request);
        LoginUserDTO loginUser = cacheService.getUserByToken(loginToken);
        return GenericDto.success(ucUserService.updateAvatar(avatarReq.getAvatar(), loginUser));
    }


    @PutMapping("/change/password")
    @ApiOperation(value = "修改密码", notes = "修改密码")
    public GenericDto<Object> updatePassword(@Valid @RequestBody ChangePasswordReq changePasswordReq, HttpServletRequest request) {
        String password = changePasswordReq.getPassword();
        String oldPassword = changePasswordReq.getOldPassword();
        ClientAuthTypeEnum authType = changePasswordReq.getAuthType();
        // 获取当前登陆人信息
        String loginToken = WebUtil.getTokenFromRequest(request);
        LoginUserDTO loginUser = cacheService.getUserByToken(loginToken);
        UcUser user = ucUserService.getById(loginUser.getUserId());
        // 判断原密码是否正确
        if (!user.getPassword().equals(PasswordUtil.getPassword(oldPassword, user.getSalt()))) {
            throw new PasswordException(UcErrorCodeEnum.ERR_10043_WRONG_OLD_PASSWORD);
        }

        // 判断code是否正确
        if (authType.equals(ClientAuthTypeEnum.EMAIL)) {
            sendCodeService.verifyEmailWithUseType(user.getEmail(), changePasswordReq.getCode(), AuthCodeUseTypeEnum.CHANGE_PASSWORD);

        } else if (authType.equals(ClientAuthTypeEnum.GA)) {
            if (!googleAuthService.verify(changePasswordReq.getCode(), user.getGaSecret())) {
                throw new LoginException(UcErrorCodeEnum.ERR_10088_WRONG_GOOGLE_AUTHENTICATOR_CODE);
            }
        } else {
            throw new InvalidArgumentsException(UcErrorCodeEnum.ERR_504_MISSING_ARGUMENTS);
        }
        // 修改密码
        return GenericDto.success(ucUserService.updatePassword(user.getId(), password));
    }

    @GetMapping("/profile-info/{gameId}")
    @ApiOperation(value = "个人游戏概况信息", notes = "个人游戏概况信息")
    public GenericDto<ProfileInfoResp> profileInfo(@PathVariable("gameId") Long gameId) {
        return GenericDto.success(ucUserService.profileInfo(UserContext.getCurrentUserId(), gameId));
    }

}
