package com.seeds.admin.service.impl;

import com.seeds.admin.dto.request.ChainMintNftReq;
import com.seeds.admin.dto.request.ChainUpdateNftReq;
import com.seeds.admin.dto.response.ChainMintNftResp;
import com.seeds.admin.service.ChainNftService;
import com.seeds.chain.config.SmartContractConfig;
import com.seeds.chain.feign.request.PinataPinJsonRequest;
import com.seeds.chain.service.GameItemsService;
import com.seeds.chain.service.IpfsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigInteger;

@Slf4j
@Service
public class ChainNftServiceImpl implements ChainNftService {

    @Autowired
    private GameItemsService gameItemsService;

    @Autowired
    private IpfsService ipfsService;

    @Autowired
    private SmartContractConfig smartContractConfig;

    @Override
    public String uploadImage(MultipartFile image) {
        // upload image to pinata and obtain hash
        return ipfsService.pinFile(image);
    }

    @Override
    public String testAuth() {
        return ipfsService.testAuth().toString();
    }

    @Override
    public String uploadMetadata(String imageFileHash, ChainMintNftReq request) {

        // generate metadata file
        PinataPinJsonRequest pinRequest = PinataPinJsonRequest.builder()
                .name(request.getName())
                .description(request.getDescription())
                .attributes(request.getAttributes())
                .image("ipfs://" + imageFileHash)
                .build();

        // upload metadata file to pinata and get hash
        return ipfsService.pinJson(pinRequest);
    }

    @Override
    public String updateMetadata(String imageFileHash, ChainUpdateNftReq updateRequest) {
        ChainMintNftReq request = ChainMintNftReq.builder()
                .name(updateRequest.getName())
                .description(updateRequest.getDescription())
                .attributes(updateRequest.getAttributes())
                .build();
        return uploadMetadata(imageFileHash, request);
    }

    @Override
    public ChainMintNftResp mintNft(String metadataFileHash) {
        // mint nft with ipfs hash
        BigInteger tokenId = gameItemsService.mintNft("ipfs://" + metadataFileHash);
        if (tokenId.compareTo(BigInteger.ZERO) > 0) {
            return generateResponse(tokenId);
        } else {
            return null;
        }
    }

    @Override
    public String getMetadataFileImageHash(String tokenId) {
        // get uri with token id
        String uri = gameItemsService.getUri(new BigInteger(tokenId));

        // get image hash of current metadata file
        return ipfsService.getImageString(uri.substring(7));
    }

    @Override
    public boolean updateNftAttribute(String tokenId, String metadataFileHash) {
        return gameItemsService.updateNftAttribute(new BigInteger(tokenId), "ipfs://" + metadataFileHash);
    }

    @Override
    public boolean burnNft(String tokenId) {
        return gameItemsService.burnNft(tokenId);
    }

    private ChainMintNftResp generateResponse(BigInteger tokenId) {
        ChainMintNftResp response = new ChainMintNftResp();
        response.setTokenId(tokenId.toString());
        response.setContractAddress(smartContractConfig.getGameAddress());
        response.setTokenStandard(smartContractConfig.getTokenStandard());
        response.setBlockchain(smartContractConfig.getBlockchain());
        response.setMetadata(smartContractConfig.getMetadataMode());
        response.setCreatorFees(smartContractConfig.getCreatorFees());
        return response;
    }
}
