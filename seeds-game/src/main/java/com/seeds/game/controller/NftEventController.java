package com.seeds.game.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.seeds.common.dto.GenericDto;
import com.seeds.common.web.context.UserContext;
import com.seeds.game.dto.request.internal.NftEventPageReq;
import com.seeds.game.dto.response.NftEventResp;
import com.seeds.game.dto.response.TypeNum;
import com.seeds.game.service.INftEventService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * nft通知 前端控制器
 * </p>
 *
 * @author hewei
 * @since 2023-03-23
 */
@Api(tags = "nft事件通知")
@RestController
@RequestMapping("/web/game/nft-event")
public class NftEventController {

    @Autowired
    private INftEventService nftEventService;

    @GetMapping("/handle-flag")
    @ApiOperation("用户是否有待处理事件")
    GenericDto<Long> getHandleFlag() {
        Long userId = UserContext.getCurrentUserId();
        return GenericDto.success(nftEventService.getHandleFlag(userId));
    }
    @GetMapping("/type-num")
    @ApiOperation("各类型数量")
    GenericDto<List<TypeNum>> getTypeNum() {
        Long userId = UserContext.getCurrentUserId();
        return GenericDto.success(nftEventService.getTypeNum(userId));
    }

    @PostMapping("/page")
    @ApiOperation("获取nft通知分页列表")
    public GenericDto<IPage<NftEventResp>> getPage(@RequestBody NftEventPageReq req) {
        req.setUserId(UserContext.getCurrentUserId());
        return GenericDto.success(nftEventService.getPage(req));
    }

    @DeleteMapping("/delete/{id}")
    @ApiOperation("删除other事件")
    GenericDto<Boolean> delete(@PathVariable("id") Long id) {
        return GenericDto.success(nftEventService.delete(id));
    }

    @PutMapping("/cancel/{id}")
    @ApiOperation("取消mint、compound")
    GenericDto<Boolean> cancel(@PathVariable("id") Long id) {
        return GenericDto.success(nftEventService.cancel(id));
    }

}
