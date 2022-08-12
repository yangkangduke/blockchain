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

import java.util.LinkedHashMap;

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
        GenericDto<Object> result = pinataApiService.authenticate(pinataConfig.getApiKey(), pinataConfig.getApiSecret());
        return result.getMessage();
    }

    @Override
    public String pinFile(MultipartFile file) {
        PinataPinResponse result = pinataApiService.pinFile(pinataConfig.getApiKey(), pinataConfig.getApiSecret(), file);
        return result.getIpfsHash();
    }
    @Override
    public String pinJson(PinataPinJsonRequest request) {
        PinataPinResponse result = pinataApiService.pinJson(pinataConfig.getApiKey(), pinataConfig.getApiSecret(), request);
        return result.getIpfsHash();
    }

    @Override
    public String getImageString(String ipfsHash) {
        LinkedHashMap result = (LinkedHashMap) pinataCloudService.getFile(ipfsHash);
        return (String) result.get("image");
    }
}
