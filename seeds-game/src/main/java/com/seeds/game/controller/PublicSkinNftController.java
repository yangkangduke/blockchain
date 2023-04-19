package com.seeds.game.controller;

import com.seeds.admin.feign.RemoteSkinNftService;
import com.seeds.common.dto.GenericDto;
import com.seeds.game.dto.request.OpenSkinNftPushAutoIdReq;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * 提供外部调用的皮肤NFT相关接口
 *
 * @author hewei
 * @date 2023/4/16
 */
@Slf4j
@Api(tags = "皮肤NFT外部调用开放")
@RestController
@RequestMapping("/public/skin/nft")
public class PublicSkinNftController {


    @Autowired
    private RemoteSkinNftService skinNftService;

    @PostMapping("push-autoId")
    @ApiOperation("推送autoId")
    public GenericDto<Object> pushAutoId(@Valid @RequestBody OpenSkinNftPushAutoIdReq req) {
        GenericDto<Object> result = skinNftService.pushAutoId(req);
        if (!result.isSuccess()) {
            return GenericDto.failure(result.getMessage(),
                    result.getCode());
        }
        return result;
    }

}
