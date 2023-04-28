package com.seeds.game.feign;

import com.seeds.admin.dto.request.GameWinRankReq;
import com.seeds.admin.dto.response.GameWinRankResp;
import com.seeds.common.dto.GenericDto;
import com.seeds.game.feign.impl.RemoteGameRankServiceImpl;
import com.seeds.game.feign.interceptor.GameFeignInnerRequestInterceptor;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;


/**
 * @author hang.yu
 * @date 2023/04/26
 */
@FeignClient(name = "remoteGameRankService", url = "${service.url.game}", fallback = RemoteGameRankServiceImpl.class, configuration = {GameFeignInnerRequestInterceptor.class})
public interface RemoteGameRankService {

	@PostMapping("/inter-game/rank/win-info")
	@ApiOperation(value = "胜场数据", notes = "胜场数据")
	GenericDto<List<GameWinRankResp.GameWinRank>> winInfo(@RequestBody GameWinRankReq query);

}
