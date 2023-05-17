package com.seeds.admin.feign;

import com.seeds.admin.dto.game.SkinNftPushAutoIdDto;
import com.seeds.admin.feign.interceptor.AdminFeignInnerRequestInterceptor;
import com.seeds.common.dto.GenericDto;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * @author hewei
 * @date 2023/4/16
 */
@FeignClient(name = "remoteSkinNftService", url = "${service.url.admin}", configuration = {AdminFeignInnerRequestInterceptor.class})
public interface RemoteSkinNftService {

	/**
	 * 游戏方推送皮肤nft的autoId
	 *
	 * @param req
	 * @return
	 */
	@PostMapping("/internal-skin-nft/push-autoId")
	@ApiOperation("NFT下架")
	GenericDto<Object> pushAutoId(SkinNftPushAutoIdDto req);
}
