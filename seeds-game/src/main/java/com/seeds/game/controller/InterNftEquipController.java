package com.seeds.game.controller;

import com.seeds.common.dto.GenericDto;
import com.seeds.common.web.inner.Inner;
import com.seeds.game.service.INftEquipmentService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @author: hewei
 * @date 2023/5/9
 */

@RestController
@RequestMapping("/inter-game/nft")
@Api(tags = "获取nft信息，内部调用")
public class InterNftEquipController {

    @Autowired
    private INftEquipmentService nftEquipmentService;

    @PostMapping("/get-owner-by-mintAddress")
    @Inner
    public GenericDto<Map<String, String>> getOwnerByMintAddress(@RequestBody List<String> mintAddresses) {
        return GenericDto.success(nftEquipmentService.getOwnerByMintAddress(mintAddresses));
    }
}
