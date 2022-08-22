package com.seeds.admin.feign;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.seeds.admin.dto.request.SysGamePageReq;
import com.seeds.admin.dto.response.SysGameBriefResp;
import com.seeds.admin.feign.impl.RemoteGameServiceImpl;
import com.seeds.admin.feign.interceptor.FeignInnerRequestInterceptor;
import com.seeds.common.dto.GenericDto;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;


/**
 * @author hang.yu
 * @date 2022/8/22
 */
@FeignClient(name = "remoteGameService", url = "${Seeds-admin}", fallback = RemoteGameServiceImpl.class, configuration = {FeignInnerRequestInterceptor.class})
public interface RemoteGameService {

	/**
	 * 获取系统游戏下拉分页列表
	 * @param query 分页查询条件
	 * @return 系统游戏信息
	 */
	@PostMapping("/game/dropdown-page")
	@ApiOperation("uc分页查询游戏")
	GenericDto<Page<SysGameBriefResp>> dropdownPage(SysGamePageReq query);

}
