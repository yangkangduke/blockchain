package com.seeds.wallet.feign;

import com.seeds.common.dto.GenericDto;
import com.seeds.wallet.config.WalletFeignConfig;
import com.seeds.wallet.dto.RawTransactionSignRequest;
import com.seeds.wallet.dto.SignMessageRequest;
import com.seeds.wallet.dto.SignedMessageDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "walletFeignClient", url = "${service.url.wallet}", configuration = WalletFeignConfig.class)
public interface WalletFeignClient {

    @PostMapping("/wallet/create")
    GenericDto<String> createAccount(@RequestParam("chain") int chain);

    @PostMapping("/wallet/sign")
    GenericDto<String> sign(@RequestBody RawTransactionSignRequest request);

    @PostMapping("/wallet/signMessages")
    GenericDto<List<SignedMessageDto>> signMessages(@RequestBody SignMessageRequest request);
}
