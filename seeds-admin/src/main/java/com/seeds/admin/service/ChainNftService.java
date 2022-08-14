package com.seeds.admin.service;

import com.seeds.admin.dto.request.ChainMintNftReq;
import com.seeds.admin.dto.request.ChainUpdateNftReq;
import org.springframework.web.multipart.MultipartFile;

public interface ChainNftService {
    String uploadImage(MultipartFile image);

    String testAuth();

    String uploadMetadata(String imageFileHash, ChainMintNftReq request);

    String updateMetadata(String imageFileHash, ChainUpdateNftReq request);

    boolean mintNft(String metadataFileHash);

    String getMetadataFileImageHash(String tokenId);

    boolean updateNftAttribute(String tokenId, String metadataFileHash);

    boolean burnNft(String tokenId);
}
