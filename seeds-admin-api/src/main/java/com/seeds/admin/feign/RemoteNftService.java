package com.seeds.admin.feign;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.seeds.admin.dto.request.NftOwnerChangeReq;
import com.seeds.admin.dto.request.UcNftPageReq;
import com.seeds.admin.dto.response.SysNftResp;
import com.seeds.admin.feign.impl.RemoteNftServiceImpl;
import com.seeds.common.dto.GenericDto;
import com.seeds.admin.feign.interceptor.FeignInnerRequestInterceptor;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author hang.yu
 * @date 2022/8/19
 */
@FeignClient(name = "remoteNftService", url = "127.0.0.1:10102", fallback = RemoteNftServiceImpl.class, configuration = {FeignInnerRequestInterceptor.class})
public interface RemoteNftService {

	/**
	 * 归属人变更
	 * @param req 归属人账户信息
	 * @return Object
	 */
	@PostMapping("/nft/owner-change")
	@ApiOperation(value = "归属人变更", notes = "归属人变更")
	GenericDto<Object> ownerChange(@RequestBody List<NftOwnerChangeReq> req);

	/**
	 * uc分页查询NFT
	 * @param query 分页查询条件
	 * @return NFT信息
	 */
	@GetMapping("/nft/uc-page")
	@ApiOperation("uc分页查询NFT")
	GenericDto<IPage<SysNftResp>> ucPage(UcNftPageReq query);

}
