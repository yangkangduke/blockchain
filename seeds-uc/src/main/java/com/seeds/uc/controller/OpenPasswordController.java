package com.seeds.uc.controller;

import com.seeds.common.dto.GenericDto;
import com.seeds.common.web.context.UserContext;
import com.seeds.uc.dto.request.PasswordChangeReq;
import com.seeds.uc.enums.UcErrorCode;
import com.seeds.uc.exceptions.PasswordException;
import com.seeds.uc.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author allen
 * @email allen.hua.ai@gmail.com
 * @date 2020/8/24
 */
@Slf4j
@RestController
@RequestMapping("/uc/password")
public class OpenPasswordController {
    @Autowired
    UserService userService;

    /**
     * 修改登陆密码
     * <p>
     * 响应码	说明
     * 504	    缺少参数
     * 10043	原密码错误
     * 10046	新旧密码相同
     * 10047	密码安全等级较低
     * 10048	密码不符合sha256要求，请重新输入密码
     *
     * @return
     */
    @PostMapping("login/change")
    public GenericDto<Object> resetLoginPassword(@RequestBody PasswordChangeReq passwordChangeReq) {
        if (StringUtils.isBlank(passwordChangeReq.getOldPassword())
                || StringUtils.isBlank(passwordChangeReq.getNewPassword())) {
            throw new PasswordException(UcErrorCode.ERR_10048_MALFORMED_PASSWORD);
        }
        Long uid = UserContext.getCurrentUserId();
        // 密码验证失败
        if (!userService.verifyPasswordByUid(uid, passwordChangeReq.getOldPassword())) {
            throw new PasswordException(UcErrorCode.ERR_10043_WRONG_OLD_PASSWORD);
        }
        userService.setPasswordByUid(uid, passwordChangeReq.getNewPassword());
        return GenericDto.success(null);
    }
}
