package com.seeds.chain.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "pinataCloudService", url = "https://gateway.pinata.cloud/ipfs/")
public interface PinataCloudService {

    @GetMapping(path = "/{hash}")
    Object getFile(@PathVariable("hash") String hash);
}
