package com.seeds.admin.feign.impl;

import com.seeds.admin.feign.RemoteKolService;
import com.seeds.common.dto.GenericDto;
import org.springframework.http.HttpStatus;


/**
 * @author hang.yu
 * @date 2022/8/19
 */
public class RemoteKolServiceImpl implements RemoteKolService {

	@Override
	public GenericDto<String> inviteCode(String inviteNo) {
		return GenericDto.failure("Internal Error:kol inviteCode failed", HttpStatus.INTERNAL_SERVER_ERROR.value());
	}
}
