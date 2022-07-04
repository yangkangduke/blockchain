package com.seeds.uc.controller;

import com.seeds.common.dto.GenericDto;
import com.seeds.common.web.context.UserContext;
import com.seeds.uc.dto.UserDto;
import com.seeds.uc.service.CacheService;
import com.seeds.uc.service.UserService;
import com.seeds.uc.util.WebUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author allen
 * @email allen.hua.ai@gmail.com
 * @date 2020/7/25
 */
@Slf4j
@RestController
@RequestMapping("/uc")
public class OpenUserController {
    @Autowired
    private UserService userService;

    @Autowired
    private CacheService cacheService;

    @PostMapping("logout")
    public GenericDto<Object> logout(HttpServletRequest request) {
        String token = WebUtil.getTokenFromRequest(request);

        cacheService.removeUserByToken(token);
        return GenericDto.success(null);
    }

    @GetMapping("user/get")
    public GenericDto<UserDto> getUserInfo() {
        Long userId = UserContext.getCurrentUserId();

        return GenericDto.success(userService.getUserByUid(userId));
    }
}
