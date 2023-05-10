package com.seeds.game.feign;

import com.seeds.common.dto.GenericDto;
import com.seeds.common.web.inner.Inner;
import com.seeds.game.feign.impl.RemoteNftEquipServiceImpl;
import com.seeds.game.feign.interceptor.GameFeignInnerRequestInterceptor;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;


/**
 * @author hewei
 * @date 2023/5/9
 */
@FeignClient(name = "RemoteNftEquipService", url = "${service.url.game}", fallback = RemoteNftEquipServiceImpl.class, configuration = {GameFeignInnerRequestInterceptor.class})
public interface RemoteNftEquipService {

	@PostMapping("/inter-game/nft/get-owner-by-mintAddress")
	@Inner
	GenericDto<Map<String, String>> getOwnerByMintAddress(@RequestBody List<String> mintAddresses);

}
