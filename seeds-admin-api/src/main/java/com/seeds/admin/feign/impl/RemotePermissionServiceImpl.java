package com.seeds.admin.feign.impl;

import com.seeds.admin.feign.RemotePermissionService;
import com.seeds.common.dto.GenericDto;
import org.springframework.http.HttpStatus;

import java.util.Set;


/**
 * @author hang.yu
 * @date 2022/8/19
 */
public class RemotePermissionServiceImpl implements RemotePermissionService {

	@Override
	public GenericDto<Set<Long>> entitledUsers(String roleCode) {
		return GenericDto.failure("Internal Error:Entitled Users failed", HttpStatus.INTERNAL_SERVER_ERROR.value());
	}
}
