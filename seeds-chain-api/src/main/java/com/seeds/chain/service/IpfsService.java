package com.seeds.chain.service;

import com.seeds.chain.feign.request.PinataPinJsonRequest;
import org.springframework.web.multipart.MultipartFile;

public interface IpfsService {
    Object testAuth();

    String pinFile(MultipartFile file);

    String pinJson(PinataPinJsonRequest request);

    String getImageString(String ipfsHash);
}
