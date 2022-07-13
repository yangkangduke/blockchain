package com.seeds.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.seeds.admin.dto.SysAdminUserDto;
import com.seeds.admin.dto.request.AdminUserQuery;
import com.seeds.admin.entity.SysAdminUserEntity;
import com.seeds.admin.enums.AdminErrorCode;
import com.seeds.admin.service.SysAdminUserService;
import com.seeds.common.dto.GenericDto;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author hang.yu
 * @date 2022/7/13
 */
@Slf4j
@RestController
@RequestMapping("/user")
public class AdminUserController {

    @Autowired
    private SysAdminUserService sysAdminUserService;

    @PostMapping("page")
    @ApiOperation("分页")
    public GenericDto<IPage<SysAdminUserDto>> queryPage(@RequestBody AdminUserQuery query){
        return GenericDto.success(sysAdminUserService.queryPage(query));
    }

    @PostMapping("add")
    @ApiOperation("添加")
    public GenericDto<SysAdminUserDto> add(@RequestBody SysAdminUserDto user){
        if (StringUtils.isEmpty(user.getMobile()) && StringUtils.isEmpty(user.getAccount())) {
            return GenericDto.failure(AdminErrorCode.ERR_504_MISSING_ARGUMENTS.getDesc(), AdminErrorCode.ERR_504_MISSING_ARGUMENTS.getCode(), null);
        }
        // 手机号查重
        if (StringUtils.isNotBlank(user.getMobile())) {
            SysAdminUserEntity adminUser = sysAdminUserService.queryByMobile(user.getMobile());
            if (adminUser != null) {
                return GenericDto.failure(AdminErrorCode.ERR_10051_PHONE_ALREADY_BEEN_USED.getDesc(), AdminErrorCode.ERR_10051_PHONE_ALREADY_BEEN_USED.getCode(), null);
            }
        }
        // 账号查重
        if (StringUtils.isNotBlank(user.getAccount())) {
            SysAdminUserEntity adminUser = sysAdminUserService.queryByAccount(user.getAccount());
            if (adminUser != null) {
                return GenericDto.failure(AdminErrorCode.ERR_10061_ACCOUNT_ALREADY_BEEN_USED.getDesc(), AdminErrorCode.ERR_10061_ACCOUNT_ALREADY_BEEN_USED.getCode(), null);
            }
        }
        return GenericDto.success(sysAdminUserService.add(user));
    }

}
