package com.seeds.game.controller;

import com.seeds.common.dto.GenericDto;
import com.seeds.game.service.IGasFeePropertyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

/**
 * @author: hewei
 * @date 2023/4/21
 */
@RestController
@Api(tags = "获取合成时gasFee")
@RequestMapping("/web/gasFee")
public class GasFeePropertyController {

    @Autowired
    private IGasFeePropertyService gasFeePropertyService;

    @GetMapping("get-price")
    @ApiOperation("获取合成时支付的gasFee")
    public GenericDto<BigDecimal> getGasFee(@RequestParam Integer type) {
        return gasFeePropertyService.getGasFee(type);
    }
}
