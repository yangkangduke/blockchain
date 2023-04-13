package com.seeds.admin.feign.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.seeds.admin.dto.request.GameWinRankReq;
import com.seeds.admin.dto.request.SysGamePageReq;
import com.seeds.admin.dto.response.*;
import com.seeds.admin.entity.SysGameApiEntity;
import com.seeds.admin.feign.RemoteGameService;
import com.seeds.common.dto.GenericDto;
import org.springframework.http.HttpStatus;

import java.util.List;


/**
 * @author hang.yu
 * @date 2022/8/19
 */
public class RemoteGameServiceImpl implements RemoteGameService {

	@Override
	public GenericDto<Page<SysGameResp>> ucPage(SysGamePageReq query) {
		return GenericDto.failure("Internal Error:game ucPage failed", HttpStatus.INTERNAL_SERVER_ERROR.value());
	}

	@Override
	public GenericDto<SysGameResp> ucDetail(Long id) {
		return GenericDto.failure("Internal Error:game ucDetail failed", HttpStatus.INTERNAL_SERVER_ERROR.value());
	}

	@Override
	public GenericDto<List<SysGameBriefResp>> ucDropdownList() {
		return GenericDto.failure("Internal Error:game uc dropdownPage failed", HttpStatus.INTERNAL_SERVER_ERROR.value());
	}

	@Override
	public GenericDto<Object> ucCollection(Long id) {
		return GenericDto.failure("Internal Error:game ucCollection failed", HttpStatus.INTERNAL_SERVER_ERROR.value());
	}

	@Override
	public GenericDto<List<SysGameTypeResp>> ucTypeDropdown() {
		return GenericDto.failure("Internal Error:game ucTypeDropdown failed", HttpStatus.INTERNAL_SERVER_ERROR.value());
	}

	@Override
	public GenericDto<String> querySecretKey(String accessKey) {
		return GenericDto.failure("Internal Error:game querySecretKey failed", HttpStatus.INTERNAL_SERVER_ERROR.value());
	}

	@Override
	public GenericDto<String> queryGameApi(Long id, Integer type) {
		return GenericDto.failure("Internal Error:game api failed", HttpStatus.INTERNAL_SERVER_ERROR.value());

	}

	@Override
	public GenericDto<SysGameApiEntity> queryByGameAndType(Long id, Integer type) {
		return GenericDto.failure("Internal Error:game entity failed", HttpStatus.INTERNAL_SERVER_ERROR.value());
	}

	@Override
	public GenericDto<List<GameWinRankResp.GameWinRank>> winRankInfo(GameWinRankReq query) {
		return GenericDto.failure("Internal Error:game rankWinInfo failed", HttpStatus.INTERNAL_SERVER_ERROR.value());
	}

	@Override
	public GenericDto<ProfileInfoResp> profileInfo(Long gameId, String email) {
		return GenericDto.failure("Internal Error:game profileInfo failed", HttpStatus.INTERNAL_SERVER_ERROR.value());
	}
}
