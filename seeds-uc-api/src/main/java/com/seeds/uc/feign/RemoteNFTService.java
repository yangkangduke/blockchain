
package com.seeds.uc.feign;


import com.seeds.common.dto.GenericDto;
import com.seeds.uc.dto.request.NFTBuyCallbackReq;
import com.seeds.uc.dto.request.NFTBuyReq;
import com.seeds.uc.dto.request.NFTForwardAuctionReq;
import com.seeds.uc.dto.request.NFTMakeOfferReq;
import com.seeds.uc.feign.interceptor.UcFeignInnerRequestInterceptor;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * @author yk
 * @date 2022/8/8
 */
@FeignClient(name = "remoteNFTService", url = "${Seeds-uc}", configuration = {UcFeignInnerRequestInterceptor.class})
public interface RemoteNFTService {

	@GetMapping("/internal-nft/buy/callback")
	@ApiOperation(value = "购买回调", notes = "购买回调")
	GenericDto<Object> buyNFTCallback(@Valid @RequestBody NFTBuyCallbackReq buyReq);

	@PostMapping("/internal-nft/buy")
	@ApiOperation(value = "购买", notes = "购买")
	GenericDto<Object> buyNFT(@Valid @RequestBody NFTBuyReq buyReq) ;

	@PostMapping("/internal-nft/forward-auction")
	@ApiOperation(value = "正向拍卖", notes = "正向拍卖")
	GenericDto<Object> forwardAuction(NFTForwardAuctionReq req);

	@PostMapping("/internal-nft/bids")
	@ApiOperation(value = "出价", notes = "出价")
	GenericDto<Object> bids(NFTMakeOfferReq req);

}
