package com.seeds.account.feign;

import com.seeds.account.dto.req.NftBuyCallbackReq;
import com.seeds.account.dto.req.NftBuyReq;
import com.seeds.common.dto.GenericDto;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

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

}
