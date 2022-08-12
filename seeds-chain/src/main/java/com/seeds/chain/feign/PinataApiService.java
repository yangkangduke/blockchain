package com.seeds.chain.feign;

import com.seeds.chain.config.PinataConfig;
import com.seeds.chain.feign.request.PinataPinJsonRequest;
import com.seeds.chain.feign.response.PinataPinResponse;
import com.seeds.common.dto.GenericDto;
import feign.Headers;
import feign.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@FeignClient(name = "pinataApiService", url = "https://api.pinata.cloud/")
@Headers({"pinata_api_key: {apiKey}", "pinata_secret_api_key: {secret}"})
public interface PinataApiService {

    @GetMapping("/data/testAuthentication")
    GenericDto<Object> authenticate(@Param("apiKey") String apiKey, @Param("secret") String secret);

//    @PostMapping(
//        path = "/pinning/pinFileToIPFS",
//        consumes = "multipart/form-data",
//        headers = {
//            "pinata_api_key=4b23467f4c734a15ea18",
//            "pinata_secret_api_key=140b427defed0d43b60c223301b45a783b47f9197429649f8fcf3dddc6092c0c"
//        }
//    )
    @PostMapping(
        path = "/pinning/pinFileToIPFS",
        consumes = "multipart/form-data"
    )
    PinataPinResponse pinFile(@Param("apiKey") String apiKey, @Param("secret") String secret, @RequestPart(value = "file")MultipartFile file);

    @PostMapping("/pinning/pinJSONToIPFS")
//    @PostMapping(
//            path = "/pinning/pinJSONToIPFS",
//            headers = {
//                "pinata_api_key=4b23467f4c734a15ea18",
//                "pinata_secret_api_key=140b427defed0d43b60c223301b45a783b47f9197429649f8fcf3dddc6092c0c"
//            }
//    )
    PinataPinResponse pinJson(@Param("apiKey") String apiKey, @Param("secret") String secret, @RequestBody PinataPinJsonRequest request);


}
