package com.seeds.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.seeds.admin.annotation.RequiredPermission;
import com.seeds.admin.dto.request.*;
import com.seeds.admin.dto.response.AdminUserResp;
import com.seeds.admin.dto.response.SysUserBriefResp;
import com.seeds.admin.dto.response.SysUserResp;
import com.seeds.admin.entity.SysUserEntity;
import com.seeds.admin.enums.AdminErrorCode;
import com.seeds.admin.utils.HashUtil;
import com.seeds.admin.service.AdminCacheService;
import com.seeds.admin.service.SysRoleUserService;
import com.seeds.admin.service.SysUserService;
import com.seeds.common.dto.GenericDto;
import com.seeds.common.web.HttpHeaders;
import com.seeds.common.web.context.UserContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

/**
 * 用户管理
 * @author hang.yu
 * @date 2022/7/13
 */
@Slf4j
@Api("用户管理")
@RestController
@RequestMapping("/user")
public class SysUserController extends AdminBaseController {

    @Value("${admin.login.init.password:123456}")
    private String initPassword;

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private AdminCacheService adminCacheService;

    @Autowired
    private SysRoleUserService sysRoleUserService;

    @PostMapping("page")
    @ApiOperation("分页")
    @RequiredPermission("sys:user:page")
    public GenericDto<IPage<SysUserResp>> queryPage(@Valid  @RequestBody SysUserPageReq query) {
        return GenericDto.success(sysUserService.queryPage(query));
    }

    @GetMapping("detail/{id}")
    @ApiOperation("信息")
    @RequiredPermission("sys:user:detail")
    public GenericDto<SysUserResp> detail(@PathVariable("id") Long id) {
        return GenericDto.success(sysUserService.detail(id));
    }

    @GetMapping("brief/{mobile}")
    @ApiOperation("简略信息")
    public GenericDto<SysUserBriefResp> brief(@PathVariable("mobile") String mobile) {
        return GenericDto.success(sysUserService.brief(mobile));
    }

    @PostMapping("add")
    @ApiOperation("添加")
    @RequiredPermission("sys:user:add")
    public GenericDto<Object> add(@Valid @RequestBody SysUserAddReq req) {
        if (StringUtils.isEmpty(req.getMobile()) && StringUtils.isEmpty(req.getAccount())) {
            return GenericDto.failure(AdminErrorCode.ERR_504_MISSING_ARGUMENTS.getDescEn(), AdminErrorCode.ERR_504_MISSING_ARGUMENTS.getCode(), null);
        }
        if (StringUtils.isEmpty(req.getInitPassport())) {
            req.setInitPassport(initPassword);
        }
        // 手机号查重
        if (StringUtils.isNotBlank(req.getMobile())) {
            SysUserEntity adminUser = sysUserService.queryByMobile(req.getMobile());
            if (adminUser != null) {
                return GenericDto.failure(AdminErrorCode.ERR_10051_PHONE_ALREADY_BEEN_USED.getDescEn(), AdminErrorCode.ERR_10051_PHONE_ALREADY_BEEN_USED.getCode(), null);
            }
        }
        // 账号查重
        if (StringUtils.isNotBlank(req.getAccount())) {
            SysUserEntity adminUser = sysUserService.queryByAccount(req.getAccount());
            if (adminUser != null) {
                return GenericDto.failure(AdminErrorCode.ERR_10061_ACCOUNT_ALREADY_BEEN_USED.getDescEn(), AdminErrorCode.ERR_10061_ACCOUNT_ALREADY_BEEN_USED.getCode(), null);
            }
        }
        sysUserService.add(req);
        return GenericDto.success(null);
    }

    @PostMapping("modify")
    @ApiOperation("编辑")
    @RequiredPermission("sys:user:modify")
    public GenericDto<Object> modify(@Valid @RequestBody SysUserModifyReq req) {
        SysUserEntity adminUser = sysUserService.queryById(req.getId());
        if (adminUser == null) {
            return GenericDto.failure(AdminErrorCode.ERR_500_SYSTEM_BUSY.getDescEn(), AdminErrorCode.ERR_500_SYSTEM_BUSY.getCode(), null);
        }
        // 手机号查重
        if (StringUtils.isNotBlank(req.getMobile())) {
            SysUserEntity mobileAdminUser = sysUserService.queryByMobile(req.getMobile());
            if (!Objects.equals(adminUser.getId(), mobileAdminUser.getId())) {
                return GenericDto.failure(AdminErrorCode.ERR_10051_PHONE_ALREADY_BEEN_USED.getDescEn(), AdminErrorCode.ERR_10051_PHONE_ALREADY_BEEN_USED.getCode(), null);
            }
        }
        sysUserService.modify(req);
        return GenericDto.success(null);
    }

    @PostMapping("changePassword")
    @ApiOperation(value = "修改密码")
    @RequiredPermission("sys:user:changePassword")
    public GenericDto<Object> changePassword(HttpServletRequest request, @Valid @RequestBody SysUserPasswordReq req) {
        SysUserEntity adminUser = sysUserService.queryById(req.getUserId());
        if (adminUser == null) {
            return GenericDto.failure(AdminErrorCode.ERR_500_SYSTEM_BUSY.getDescEn(), AdminErrorCode.ERR_500_SYSTEM_BUSY.getCode(), null);
        }
        String password = HashUtil.sha256(req.getOldPassword() + adminUser.getSalt());
        if (!password.equals(adminUser.getPassword())) {
            return GenericDto.failure(AdminErrorCode.ERR_10043_WRONG_OLD_PASSWORD.getDescEn(), AdminErrorCode.ERR_10043_WRONG_OLD_PASSWORD.getCode(), null);
        }
        sysUserService.updatePassword(adminUser, req.getNewPassword());
        // 登出
        adminCacheService.removeAdminUserByUserId(req.getUserId());
        return GenericDto.success(null);
    }

    @PostMapping("resetPassword")
    @ApiOperation(value = "重置密码")
    @RequiredPermission("sys:user:resetPassword")
    public GenericDto<Object> resetPassword(HttpServletRequest request) {
        String userId = request.getHeader(HttpHeaders.ADMIN_USER_ID);
        SysUserEntity adminUser = sysUserService.queryById(Long.valueOf(userId));
        if (adminUser == null) {
            return GenericDto.failure(AdminErrorCode.ERR_500_SYSTEM_BUSY.getDescEn(), AdminErrorCode.ERR_500_SYSTEM_BUSY.getCode(), null);
        }
        sysUserService.updatePassword(adminUser, initPassword);
        // 登出
        String token = request.getHeader(HttpHeaders.ADMIN_USER_TOKEN);
        adminCacheService.removeAdminUserByToken(token);
        return GenericDto.success(null);
    }

    @PostMapping("delete")
    @ApiOperation("删除")
    @RequiredPermission("sys:user:delete")
    public GenericDto<Object> delete(@RequestBody ListReq req) {
        sysUserService.batchDelete(req.getIds());
        return GenericDto.success(null);
    }

    @PostMapping("switch")
    @ApiOperation("启用/停用")
    @RequiredPermission("sys:user:switch")
    public GenericDto<Object> enableOrDisable(@Valid @RequestBody List<SwitchReq> req) {
        sysUserService.enableOrDisable(req);
        // 批量登出
        req.forEach(p -> adminCacheService.removeAdminUserByUserId(p.getId()));
        return GenericDto.success(null);
    }

    @PostMapping("updateRoles")
    @ApiOperation("授予/剥夺角色")
    @RequiredPermission("sys:user:updateRoles")
    public GenericDto<Object> updateRoles(@Valid @RequestBody SysUserRoleReq req) {
        sysRoleUserService.updateRoles(req);
        return GenericDto.success(null);
    }

    @GetMapping("userInfo")
    @ApiOperation(value = "登录用户信息")
    public GenericDto<AdminUserResp> getUserInfo() {
        Long userId = UserContext.getCurrentAdminUserId();
        return GenericDto.success(sysUserService.queryLoginUserInfo(userId));
    }

}