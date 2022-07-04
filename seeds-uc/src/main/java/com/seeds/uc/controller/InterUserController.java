package com.seeds.uc.controller;

import com.seeds.common.dto.GenericDto;
import com.seeds.uc.dto.InterUserInfo;
import com.seeds.uc.dto.mapstruct.UserDtoMapper;
import com.seeds.uc.mapper.UserMapper;
import com.seeds.uc.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author allen
 * @email allen.hua.ai@gmail.com
 * @date 2020/8/26
 */
@Slf4j
@RestController
@RequestMapping("/uc-internal/user/")
public class InterUserController {

    @Autowired
    UserDtoMapper userDtoMapper;

    @Autowired
    UserMapper userMapper;

    @Autowired
    UserService userService;

    @GetMapping("get")
    public GenericDto<InterUserInfo> getUserInfo(@RequestParam("uid") Long uid) {
        return GenericDto.success(userService.getInternalUserInfo(uid));
    }

    @PostMapping("freeze")
    public GenericDto<Boolean> freezeUser(@RequestParam("uid") Long uid) {
        userService.freezeUser(uid);
        return GenericDto.success(true);
    }
}
