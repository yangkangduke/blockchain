package com.seeds.game.feign;

import com.seeds.common.dto.GenericDto;
import com.seeds.game.entity.NftPublicBackpackEntity;
import com.seeds.game.feign.impl.RemoteNftBackpackServiceImpl;
import com.seeds.game.feign.interceptor.GameFeignInnerRequestInterceptor;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.math.BigDecimal;
import java.util.List;


/**
 * @author hewei
 * @date 2023/5/9
 */
@FeignClient(name = "RemoteNftBackpackService", url = "${service.url.game}", fallback = RemoteNftBackpackServiceImpl.class, configuration = {GameFeignInnerRequestInterceptor.class})
public interface RemoteNftBackpackService {

	@PostMapping("/inter-game/nft/insert-backpack")
	GenericDto<Object> insertBackpack(@RequestBody List<NftPublicBackpackEntity> backpackEntity);

	@PostMapping("/inter-game/nft/usd-rate/{currency}")
	@ApiOperation("获取美元汇率")
	GenericDto<BigDecimal> usdRate(@PathVariable String currency);
}
