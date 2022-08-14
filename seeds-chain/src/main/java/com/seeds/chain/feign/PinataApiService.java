package com.seeds.chain.feign;

import com.seeds.chain.feign.request.PinataPinJsonRequest;
import com.seeds.chain.feign.response.PinataPinResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@FeignClient(name = "pinataApiService", url = "https://api.pinata.cloud/")
public interface PinataApiService {

    @GetMapping("/data/testAuthentication")
    Object authenticate(@RequestHeader Map<String, String> headerMap);

    @PostMapping(
        path = "/pinning/pinFileToIPFS",
        consumes = "multipart/form-data"
    )
    PinataPinResponse pinFile(@RequestHeader Map<String, String> headerMap, @RequestPart(value = "file")MultipartFile file);

    @PostMapping("/pinning/pinJSONToIPFS")
    PinataPinResponse pinJson(@RequestHeader Map<String, String> headerMap, @RequestBody PinataPinJsonRequest request);


}
