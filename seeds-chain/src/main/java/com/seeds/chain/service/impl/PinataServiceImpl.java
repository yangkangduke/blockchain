package com.seeds.chain.service.impl;

import com.seeds.chain.config.PinataConfig;
import com.seeds.chain.feign.PinataApiService;
import com.seeds.chain.feign.PinataCloudService;
import com.seeds.chain.feign.request.PinataPinJsonRequest;
import com.seeds.chain.feign.response.PinataPinResponse;
import com.seeds.chain.service.IpfsService;
import com.seeds.common.dto.GenericDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
@Service
public class PinataServiceImpl implements IpfsService {

    @Autowired
    PinataApiService pinataApiService;

    @Autowired
    PinataCloudService pinataCloudService;

    @Autowired
    PinataConfig pinataConfig;

    @Override
    public Object testAuth() {
        Object result = pinataApiService.authenticate(securityHeaders());
        return result;
    }

    @Override
    public String pinFile(MultipartFile file) {
        PinataPinResponse result = pinataApiService.pinFile(securityHeaders(), file);
        return result.getIpfsHash();
    }

    @Override
    public String pinJson(PinataPinJsonRequest request) {
        PinataPinResponse result = pinataApiService.pinJson(securityHeaders(), request);
        return result.getIpfsHash();
    }
    @Override
    public String getImageString(String ipfsHash) {
        LinkedHashMap result = (LinkedHashMap) pinataCloudService.getFile(ipfsHash);
        return (String) result.get("image");
    }

    private Map<String, String> securityHeaders() {
        Map<String, String> headers = new HashMap<>();
        headers.put("pinata_api_key", pinataConfig.getApiKey());
        headers.put("pinata_secret_api_key", pinataConfig.getApiSecret());
        return headers;
    }
}
