package com.seeds.admin.feign.impl;

import com.seeds.admin.dto.request.RandomCodeUseReq;
import com.seeds.admin.feign.RemoteRandomCodeService;
import com.seeds.common.dto.GenericDto;
import org.springframework.http.HttpStatus;


/**
 * @author hang.yu
 * @date 2022/8/19
 */
public class RemoteRandomCodeServiceImpl implements RemoteRandomCodeService {

	@Override
	public GenericDto<Object> useRandomCode(RandomCodeUseReq req) {
		return GenericDto.failure("Internal Error:Use random code failed", HttpStatus.INTERNAL_SERVER_ERROR.value());
	}
}
