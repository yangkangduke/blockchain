package com.seeds.admin.feign.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.seeds.admin.dto.request.SysGamePageReq;
import com.seeds.admin.dto.response.SysGameBriefResp;
import com.seeds.admin.dto.response.SysGameResp;
import com.seeds.admin.feign.RemoteGameService;
import com.seeds.common.dto.GenericDto;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.List;


/**
 * @author hang.yu
 * @date 2022/8/19
 */
@Component
public class RemoteGameServiceImpl implements RemoteGameService {

	@Override
	public GenericDto<Page<SysGameResp>> ucPage(SysGamePageReq query) {
		return GenericDto.failure("Internal Error:ucPage failed", HttpStatus.INTERNAL_SERVER_ERROR.value());
	}

	@Override
	public GenericDto<SysGameResp> ucDetail(Long id) {
		return GenericDto.failure("Internal Error:ucDetail failed", HttpStatus.INTERNAL_SERVER_ERROR.value());
	}

	@Override
	public GenericDto<List<SysGameBriefResp>> ucDropdownList() {
		return GenericDto.failure("Internal Error:uc dropdownPage failed", HttpStatus.INTERNAL_SERVER_ERROR.value());
	}
}
