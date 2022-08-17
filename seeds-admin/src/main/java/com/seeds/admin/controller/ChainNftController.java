package com.seeds.admin.controller;

import cn.hutool.json.JSONUtil;
import com.seeds.admin.dto.request.ChainBurnNftReq;
import com.seeds.admin.dto.request.ChainMintNftReq;
import com.seeds.admin.dto.request.ChainUpdateNftReq;
import com.seeds.admin.service.ChainNftService;
import com.seeds.common.dto.GenericDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static com.seeds.admin.enums.AdminErrorCodeEnum.ERR_90001_FAIL_TO_EXECUTE_ON_CHAIN;


@Slf4j
@Api(tags = "区块链")
@RestController
@RequestMapping("/chain")
public class ChainNftController {

    @Autowired
    private ChainNftService chainNftService;

    @PostMapping("mint-nft")
    @ApiOperation(value = "mint-nft", notes = "metaData参数格式：{\"name\":\"bow\", \"description\":\"shortbow\", \"attributes\": [{\"trait_type\":\"element\",\"value\":\"fire\"}]}")
    public GenericDto<Object> mintNewNft(
        @RequestPart("image") MultipartFile image,
        @RequestParam String metaData
    ) {
        log.info("metaData: {}", metaData);
        ChainMintNftReq chainMintNftReq = JSONUtil.toBean(metaData, ChainMintNftReq.class);
        String imageFileHash = chainNftService.uploadImage(image);
        String metadataFileHash = chainNftService.uploadMetadata(imageFileHash, chainMintNftReq);
        boolean status = chainNftService.mintNft(metadataFileHash);

        if (status) {
            return GenericDto.success(null);
        } else {
            return GenericDto.failure(ERR_90001_FAIL_TO_EXECUTE_ON_CHAIN.getDescEn(), ERR_90001_FAIL_TO_EXECUTE_ON_CHAIN.getCode(), null);
        }
    }

    @PostMapping("/update-nft")
    @ApiOperation(value = "update-nft", notes = "update-nft")
    public GenericDto<Object> updateNftAttribute(@RequestBody ChainUpdateNftReq request) {
        String imageFileHash = chainNftService.getMetadataFileImageHash(request.getTokenId());
        String metadataFileHash = chainNftService.updateMetadata(imageFileHash, request);
        boolean status = chainNftService.updateNftAttribute(request.getTokenId(), metadataFileHash);

        if (status) {
            return GenericDto.success(null);
        } else {
            return GenericDto.failure(ERR_90001_FAIL_TO_EXECUTE_ON_CHAIN.getDescEn(), ERR_90001_FAIL_TO_EXECUTE_ON_CHAIN.getCode(), null);
        }
    }

    @PostMapping("/burn-nft")
    @ApiOperation(value = "burn-nft", notes = "burn-nft")
    public GenericDto<Object> burnNft(@RequestBody ChainBurnNftReq request) {
        boolean status = chainNftService.burnNft(request.getTokenId());

        if (status) {
            return GenericDto.success(null);
        } else {
            return GenericDto.failure(ERR_90001_FAIL_TO_EXECUTE_ON_CHAIN.getDescEn(), ERR_90001_FAIL_TO_EXECUTE_ON_CHAIN.getCode(), null);
        }
    }

    @GetMapping("/test-auth")
    @ApiOperation(value = "test-auth", notes = "test-auth")
    public GenericDto<Object> testAuth() {
        try {
            return GenericDto.success(chainNftService.testAuth());
        } catch (Exception e) {
            return GenericDto.failure(ERR_90001_FAIL_TO_EXECUTE_ON_CHAIN.getDescEn(), ERR_90001_FAIL_TO_EXECUTE_ON_CHAIN.getCode(), null);
        }
    }
}
