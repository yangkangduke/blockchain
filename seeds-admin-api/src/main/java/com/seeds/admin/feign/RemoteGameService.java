package com.seeds.admin.feign;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.seeds.admin.dto.request.SysGamePageReq;
import com.seeds.admin.dto.response.SysGameBriefResp;
import com.seeds.admin.dto.response.SysGameResp;
import com.seeds.admin.dto.response.SysGameTypeResp;
import com.seeds.admin.feign.impl.RemoteGameServiceImpl;
import com.seeds.admin.feign.interceptor.FeignInnerRequestInterceptor;
import com.seeds.common.dto.GenericDto;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


/**
 * @author hang.yu
 * @date 2022/8/22
 */
@FeignClient(name = "remoteGameService", url = "${Seeds-admin}", fallback = RemoteGameServiceImpl.class, configuration = {FeignInnerRequestInterceptor.class})
public interface RemoteGameService {

	/**
	 * 分页获取系统游戏信息
	 * @param query 分页查询条件
	 * @return 系统游戏信息
	 */
	@PostMapping("/internal-game/uc-page")
	@ApiOperation("uc查询游戏分页")
	GenericDto<Page<SysGameResp>> ucPage(@RequestBody SysGamePageReq query);

	/**
	 * 通过游戏id获取系统游戏信息
	 * @param id 游戏id
	 * @return 系统游戏信息
	 */
	@GetMapping("/internal-game/uc-detail")
	@ApiOperation("uc查询游戏信息")
	GenericDto<SysGameResp> ucDetail(@RequestParam Long id);

	/**
	 * 获取系统游戏下拉列表
	 * @return 系统游戏信息
	 */
	@PostMapping("/internal-game/uc-dropdown-list")
	@ApiOperation("uc查询游戏下拉列表")
	GenericDto<List<SysGameBriefResp>> ucDropdownList();

	/**
	 * uc收藏
	 * @param id 游戏的id
	 * @return 系统游戏
	 */
	@PostMapping("/internal-game/uc-collection")
	@ApiOperation("uc收藏")
	GenericDto<Object> ucCollection(@RequestParam Long id);

	/**
	 * 获取系统游戏类别列表
	 * @return 系统游戏类别列表
	 */
	@GetMapping("/internal-game-type/uc-dropdown")
	@ApiOperation("uc查询游戏类别列表")
	GenericDto<List<SysGameTypeResp>> ucTypeDropdown();

	/**
	 * 查询游戏秘钥
	 * @param accessKey 游戏的访问键
	 * @return 系统游戏秘钥
	 */
	@GetMapping("/internal-game/secret-key")
	@ApiOperation("查询游戏秘钥")
	GenericDto<String> querySecretKey(@RequestParam String accessKey);

}
