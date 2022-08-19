package com.seeds.admin.feign.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.seeds.admin.dto.request.NftOwnerChangeReq;
import com.seeds.admin.dto.request.UcNftPageReq;
import com.seeds.admin.dto.response.SysNftResp;
import com.seeds.admin.feign.RemoteNftService;
import com.seeds.common.dto.GenericDto;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author hang.yu
 * @date 2022/8/19
 */
@Component
public class RemoteNftServiceImpl implements RemoteNftService {


	@Override
	public GenericDto<Object> ownerChange(List<NftOwnerChangeReq> req) {
		return GenericDto.failure("Internal Error:ownerChange failed", HttpStatus.INTERNAL_SERVER_ERROR.value());
	}

	@Override
	public GenericDto<IPage<SysNftResp>> ucPage(UcNftPageReq query) {
		return GenericDto.failure("Internal Error:ucPage failed", HttpStatus.INTERNAL_SERVER_ERROR.value());

	}
}
