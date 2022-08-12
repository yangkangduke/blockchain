package com.seeds.admin.controller;

import com.seeds.admin.dto.request.ChainBurnNftReq;
import com.seeds.admin.dto.request.ChainMintNftReq;
import com.seeds.admin.dto.request.ChainUpdateNftReq;
import com.seeds.admin.service.ChainNftService;
import com.seeds.common.dto.GenericDto;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static com.seeds.admin.enums.AdminErrorCodeEnum.ERR_90001_FAIL_TO_EXECUTE_ON_CHAIN;


@Slf4j
@Api("区块链管理")
@RestController
@RequestMapping("/chain")
public class ChainNftController {

    @Autowired
    private ChainNftService chainNftService;

    @PostMapping("mint-nft")
    public GenericDto<Object> mintNewNft(
        @RequestPart("image") MultipartFile image,
        @RequestPart("metaData") ChainMintNftReq request
    ) {
        log.info("metaData: {}", request);
        String imageFileHash = chainNftService.uploadImage(image);
        String metadataFileHash = chainNftService.uploadMetadata(imageFileHash, request);
//        String imageFileHash = "QmQG18b7QfJ8xRBN3mnDNEkXSotdw9GVE8Pb4w4oA2KbAa";
//        String metadataFileHash = "QmXuw8iSyDsRyjF5Q3cVAzw5YedFgV5RZPiRU4jdx7REqt";
        boolean status = chainNftService.mintNft(metadataFileHash);

        if (status) {
            return GenericDto.success(null);
        } else {
            return GenericDto.failure(ERR_90001_FAIL_TO_EXECUTE_ON_CHAIN.getDescEn(), ERR_90001_FAIL_TO_EXECUTE_ON_CHAIN.getCode(), null);
        }
    }

    @PostMapping("/update-nft")
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
    public GenericDto<Object> burnNft(@RequestBody ChainBurnNftReq request) {
        boolean status = chainNftService.burnNft(request.getTokenId());

        if (status) {
            return GenericDto.success(null);
        } else {
            return GenericDto.failure(ERR_90001_FAIL_TO_EXECUTE_ON_CHAIN.getDescEn(), ERR_90001_FAIL_TO_EXECUTE_ON_CHAIN.getCode(), null);
        }
    }

}
