package com.seeds.admin.feign.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.seeds.admin.dto.request.NftOwnerChangeReq;
import com.seeds.admin.dto.request.SysNftPageReq;
import com.seeds.admin.dto.request.UcSwitchReq;
import com.seeds.admin.dto.response.SysNftDetailResp;
import com.seeds.admin.dto.response.SysNftResp;
import com.seeds.admin.dto.response.SysNftTypeResp;
import com.seeds.admin.feign.RemoteNftService;
import com.seeds.common.dto.GenericDto;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

/**
 * @author hang.yu
 * @date 2022/8/19
 */
@Component
public class RemoteNftServiceImpl implements RemoteNftService {


	@PostMapping("/internal-nft/owner-change")
	@Override
	public GenericDto<Object> ownerChange(List<NftOwnerChangeReq> req) {
		return GenericDto.failure("Internal Error:nft ownerChange failed", HttpStatus.INTERNAL_SERVER_ERROR.value());
	}

	@Override
	public GenericDto<Page<SysNftResp>> ucPage(SysNftPageReq query) {
		return GenericDto.failure("Internal Error:nft ucPage failed", HttpStatus.INTERNAL_SERVER_ERROR.value());
	}

	@Override
	public GenericDto<SysNftDetailResp> ucDetail(Long id) {
		return GenericDto.failure("Internal Error:nft ucDetail failed", HttpStatus.INTERNAL_SERVER_ERROR.value());
	}

	@Override
	public GenericDto<List<SysNftTypeResp>> ucTypeDropdown() {
		return GenericDto.failure("Internal Error:nft ucNftType dropdown failed", HttpStatus.INTERNAL_SERVER_ERROR.value());
	}

	@Override
	public GenericDto<Object> ucCollection(Long id) {
		return GenericDto.failure("Internal Error:nft ucCollection failed", HttpStatus.INTERNAL_SERVER_ERROR.value());
	}

	@Override
	public GenericDto<Object> ucView(Long id) {
		return GenericDto.failure("Internal Error:nft ucView failed", HttpStatus.INTERNAL_SERVER_ERROR.value());
	}

	@Override
	public GenericDto<Object> ucUpOrDown(UcSwitchReq req) {
		return GenericDto.failure("Internal Error:nft ucUpOrDown failed", HttpStatus.INTERNAL_SERVER_ERROR.value());
	}

}
