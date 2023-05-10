package com.seeds.game.controller;

import com.seeds.common.dto.GenericDto;
import com.seeds.common.web.inner.Inner;
import com.seeds.game.entity.NftPublicBackpackEntity;
import com.seeds.game.service.INftPublicBackpackService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author: hewei
 * @date 2023/5/9
 */

@RestController
@RequestMapping("/inter-game/nft")
@Api(tags = "公共背包插入数据，内部调用")
public class InterNftPublicBackpackController {

    @Autowired
    private INftPublicBackpackService backpackService;

    @PostMapping("/insert-backpack")

    @Inner
    public GenericDto<Object> insertBackpack(@RequestBody List<NftPublicBackpackEntity> backpackEntities) {
        backpackService.insertBackpack(backpackEntities);
        return GenericDto.success(null);
    }
}
