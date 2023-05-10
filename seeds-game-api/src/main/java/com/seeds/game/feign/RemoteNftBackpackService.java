package com.seeds.game.feign;

import com.seeds.common.dto.GenericDto;
import com.seeds.common.web.inner.Inner;
import com.seeds.game.entity.NftPublicBackpackEntity;
import com.seeds.game.feign.impl.RemoteNftBackpackServiceImpl;
import com.seeds.game.feign.interceptor.GameFeignInnerRequestInterceptor;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;


/**
 * @author hewei
 * @date 2023/5/9
 */
@FeignClient(name = "RemoteNftBackpackService", url = "${service.url.game}", fallback = RemoteNftBackpackServiceImpl.class, configuration = {GameFeignInnerRequestInterceptor.class})
public interface RemoteNftBackpackService {

	@PostMapping("/inter-game/nft/insert-backpack")
	@Inner
	GenericDto<Object> insertBackpack(@RequestBody List<NftPublicBackpackEntity> backpackEntity);
}
