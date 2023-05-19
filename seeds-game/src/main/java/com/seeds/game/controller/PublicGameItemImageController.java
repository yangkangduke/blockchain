package com.seeds.game.controller;

import com.seeds.common.dto.GenericDto;
import com.seeds.game.entity.ItemImage;
import com.seeds.game.service.ItemImageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * <p>
 * 游戏资源图片 前端控制器
 * </p>
 *
 * @author hang.yu
 * @date 2023-5-19
 */
@RestController
@RequestMapping("/public/web/game-item-image")
@Api(tags = "游戏资源图片")
public class PublicGameItemImageController {

    @Autowired
    private ItemImageService itemImageService;

    @GetMapping("/list")
    @ApiOperation(value = "列表", notes = "列表")
    public GenericDto<List<ItemImage>> winInfo() {
        return GenericDto.success(itemImageService.list());
    }

}
