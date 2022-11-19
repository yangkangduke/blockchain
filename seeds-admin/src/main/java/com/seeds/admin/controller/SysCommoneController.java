package com.seeds.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.seeds.common.dto.GenericDto;
import com.seeds.common.web.inner.Inner;
import com.seeds.uc.dto.request.AllUserReq;
import com.seeds.uc.dto.response.UcUserResp;
import com.seeds.uc.feign.UserCenterFeignClient;
import com.seeds.uc.model.UcUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @program: seeds-java
 * @description: 公共
 * @author: yk
 * @create: 2022-11-18 12:27
 **/
@Slf4j
@RestController
@RequestMapping("/commone")
@Api(tags = "commone")
public class SysCommoneController {

    @Autowired
    private UserCenterFeignClient userCenterFeignClient;

    @PostMapping("/all-user")
    @ApiOperation("获取UC端所有用户")
    public GenericDto<Page<UcUserResp>> getAllUser(@RequestBody AllUserReq allUserReq) {
        GenericDto<Page<UcUserResp>> allUser = userCenterFeignClient.getAllUser(allUserReq);
        return GenericDto.success(allUser.getData());
    }
}
