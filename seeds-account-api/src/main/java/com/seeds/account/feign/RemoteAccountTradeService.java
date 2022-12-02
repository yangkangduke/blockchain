package com.seeds.account.feign;

import com.seeds.account.dto.NftGasFeesDto;
import com.seeds.account.dto.req.*;
import com.seeds.account.dto.resp.NftAuctionResp;
import com.seeds.account.dto.resp.NftOfferResp;
import com.seeds.common.dto.GenericDto;
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
@FeignClient(name = "remoteAccountTradeService", url = "${service.url.account}", path = "/account-trade-internal", configuration = {AccountFeignInnerRequestInterceptor.class})
public interface RemoteAccountTradeService {

	@PostMapping("/buy-nft-callback")
	@ApiOperation(value = "购买NFT回调", notes = "购买NFT回调")
	GenericDto<Object> buyNftCallback(@Valid @RequestBody NftBuyCallbackReq buyReq);

	@PostMapping("/buy-nft")
	@ApiOperation(value = "购买NFT", notes = "购买NFT")
	GenericDto<Object> buyNft(@Valid @RequestBody NftBuyReq buyReq);

	@PostMapping("/nft-forward-auction")
	@ApiOperation(value = "正向拍卖", notes = "正向拍卖")
	GenericDto<Object> forwardAuction(NftForwardAuctionReq req);

	@PostMapping("/nft-reverse-auction")
	@ApiOperation(value = "反向拍卖", notes = "反向拍卖")
	GenericDto<Object> reverseAuction(NftReverseAuctionReq req);

	@PostMapping("/nft-forward-bids")
	@ApiOperation(value = "正向出价", notes = "正向出价")
	GenericDto<Object> forwardBids(NftMakeOfferReq req);

	@PostMapping("/nft-reverse-bids")
	@ApiOperation(value = "反向出价", notes = "反向出价")
	GenericDto<Object> reverseBids(NftBuyReq req);

	@GetMapping("/nft-offer-list")
	@ApiOperation("NFT出价列表")
	GenericDto<List<NftOfferResp>> offerList(@RequestParam Long id);

	@GetMapping("/nft-action-info")
	@ApiOperation("NFT拍卖信息")
	GenericDto<NftAuctionResp> actionInfo(@RequestParam Long id, @RequestParam Long userId);

	@PostMapping("/nft-deduct-gas-fee")
	@ApiOperation(value = "扣除手续费", notes = "扣除手续费")
	GenericDto<Object> nftDeductGasFee(@Valid @RequestBody AccountOperateReq req);

	@GetMapping("/nft-gas-fees")
	@ApiOperation(value = "NFT手续费", notes = "NFT手续费")
	GenericDto<List<NftGasFeesDto>> nftGasFee();

	@PostMapping("/amount-unfreeze")
	@ApiOperation(value = "解冻金额", notes = "解冻金额")
	GenericDto<Object> amountUnfreeze(@Valid @RequestBody AccountOperateReq req);

	@PostMapping("/amount-change-balance")
	@ApiOperation(value = "余额变更", notes = "余额变更")
	GenericDto<Object> amountChangeBalance(@Valid @RequestBody AccountOperateReq req);

	@PostMapping("/job/nft-offer-expired")
	@ApiOperation(value = "NFT的offer过期任务", notes = "NFT的offer过期任务")
	GenericDto<Object> nftOfferExpired();

}
