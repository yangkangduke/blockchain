package com.seeds.admin.feign;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.seeds.admin.dto.request.NftOwnerChangeReq;
import com.seeds.admin.dto.request.SysNftPageReq;
import com.seeds.admin.dto.request.UcSwitchReq;
import com.seeds.admin.dto.response.SysNftDetailResp;
import com.seeds.admin.dto.response.SysNftResp;
import com.seeds.admin.dto.response.SysNftTypeResp;
import com.seeds.admin.feign.impl.RemoteNftServiceImpl;
import com.seeds.common.dto.GenericDto;
import com.seeds.admin.feign.interceptor.FeignInnerRequestInterceptor;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @author hang.yu
 * @date 2022/8/19
 */
@FeignClient(name = "remoteNftService", url = "${Seeds-admin}", fallback = RemoteNftServiceImpl.class, configuration = {FeignInnerRequestInterceptor.class})
public interface RemoteNftService {

	/**
	 * 归属人变更
	 * @param req 归属人账户信息
	 * @return Object
	 */
	@PostMapping("/internal-nft/owner-change")
	@ApiOperation(value = "归属人变更", notes = "归属人变更")
	GenericDto<Object> ownerChange(@RequestBody List<NftOwnerChangeReq> req);

	/**
	 * uc分页查询NFT
	 * @param query 分页查询条件
	 * @return NFT信息
	 */
	@PostMapping("/internal-nft/uc-page")
	@ApiOperation("uc分页查询NFT")
	GenericDto<Page<SysNftResp>> ucPage(@RequestBody SysNftPageReq query);

	/**
	 * 通过id获取系统NFT信息
	 * @param id NFT的id
	 * @return 系统NFT信息
	 */
	@GetMapping("/internal-nft/uc-detail")
	@ApiOperation("uc查询NFT信息")
	GenericDto<SysNftDetailResp> ucDetail(@RequestParam Long id);

	/**
	 * 获取系统NFT类别列表
	 * @return 系统NFT类别列表
	 */
	@GetMapping("/internal-nft-type/uc-dropdown")
	@ApiOperation("uc查询NFT类别列表")
	GenericDto<List<SysNftTypeResp>> ucTypeDropdown();

	/**
	 * uc收藏
	 * @param id NFT的id
	 * @return 系统NFT信息
	 */
	@PostMapping("/internal-nft/uc-collection")
	@ApiOperation("uc收藏")
	GenericDto<Object> ucCollection(@RequestParam Long id);

	/**
	 * uc浏览
	 * @param id NFT的id
	 * @return 系统NFT信息
	 */
	@PostMapping("/internal-nft/uc-view")
	@ApiOperation("uc浏览")
	GenericDto<Object> ucView(@RequestParam Long id);

	/**
	 * uc上架/下架
	 * @param req NFT的id列表和状态
	 * @return 系统NFT信息
	 */
	@PostMapping("/internal-nft/uc-switch")
	@ApiOperation("uc上架/下架")
	GenericDto<Object> ucUpOrDown(@Valid @RequestBody UcSwitchReq req);

}
