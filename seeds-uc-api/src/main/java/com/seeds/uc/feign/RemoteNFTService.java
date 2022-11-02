
package com.seeds.uc.feign;


import com.seeds.common.dto.GenericDto;
import com.seeds.uc.dto.request.*;
import com.seeds.uc.dto.response.NFTAuctionResp;
import com.seeds.uc.dto.response.NFTOfferResp;
import com.seeds.uc.feign.interceptor.UcFeignInnerRequestInterceptor;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.List;

/**
 * @author yk
 * @date 2022/8/8
 */
@FeignClient(name = "remoteNFTService", url = "${Seeds-uc}", configuration = {UcFeignInnerRequestInterceptor.class})
public interface RemoteNFTService {

	@PostMapping("/internal-nft/buy/callback")
	@ApiOperation(value = "购买回调", notes = "购买回调")
	GenericDto<Object> buyNFTCallback(@Valid @RequestBody NFTBuyCallbackReq buyReq);

	@PostMapping("/internal-nft/buy")
	@ApiOperation(value = "购买", notes = "购买")
	GenericDto<Object> buyNFT(@Valid @RequestBody NFTBuyReq buyReq) ;

	@PostMapping("/internal-nft/deduct-gas-fee")
	@ApiOperation(value = "扣除手续费", notes = "扣除手续费")
	GenericDto<Object> deductGasFee(@Valid @RequestBody NFTDeductGasFeeReq req) ;

	@PostMapping("/internal-nft/forward-auction")
	@ApiOperation(value = "正向拍卖", notes = "正向拍卖")
	GenericDto<Object> forwardAuction(NFTForwardAuctionReq req);

	@PostMapping("/internal-nft/reverse-auction")
	@ApiOperation(value = "反向拍卖", notes = "反向拍卖")
	GenericDto<Object> reverseAuction(NFTReverseAuctionReq req);

	@PostMapping("/internal-nft/forward-bids")
	@ApiOperation(value = "正向出价", notes = "正向出价")
	GenericDto<Object> forwardBids(NFTMakeOfferReq req);

	@PostMapping("/internal-nft/reverse-bids")
	@ApiOperation(value = "反向出价", notes = "反向出价")
	GenericDto<Object> reverseBids(NFTBuyReq req);

	@GetMapping("/internal-nft/offer-list")
	@ApiOperation("NFT出价列表")
	GenericDto<List<NFTOfferResp>> offerList(@RequestParam Long id);

	@GetMapping("/internal-nft/action-info")
	@ApiOperation("NFT拍卖信息")
	GenericDto<NFTAuctionResp> actionInfo(@RequestParam Long id, @RequestParam Long userId);

    @PostMapping("/internal-nft/shelves")
    @ApiOperation("NFT上架")
    GenericDto<Object> shelves(NFTShelvesReq req);

    @PostMapping("/internal-nft/sold-out")
    @ApiOperation("NFT下架")
    GenericDto<Object> soldOut(NFTSoldOutReq req);

}
