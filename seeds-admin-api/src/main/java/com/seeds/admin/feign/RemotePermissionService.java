package com.seeds.admin.feign;

import com.seeds.admin.feign.impl.RemotePermissionServiceImpl;
import com.seeds.admin.feign.interceptor.AdminFeignInnerRequestInterceptor;
import com.seeds.common.dto.GenericDto;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Set;


/**
 * @author hang.yu
 * @date 2022/11/10
 */
@FeignClient(name = "remotePermissionService", url = "${service.url.admin}", fallback = RemotePermissionServiceImpl.class, configuration = {AdminFeignInnerRequestInterceptor.class})
public interface RemotePermissionService {

	/**
	 * 获取拥有指定角色的用户
	 * @param roleCode 角色
	 * @return 用户id列表
	 */
	@GetMapping("/internal-permission/entitled-users")
	@ApiOperation(value = "获取拥有指定权限的用户", notes = "获取拥有指定权限的用户")
	GenericDto<Set<Long>> entitledUsers(@RequestParam String roleCode);

}
