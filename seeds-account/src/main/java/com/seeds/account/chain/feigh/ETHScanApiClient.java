package com.seeds.account.chain.feigh;

import com.seeds.account.dto.EtherscanGasPriceResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

public interface ETHScanApiClient {
    @GetMapping("/api")
    EtherscanGasPriceResponse getGasPrice(@RequestParam("module") String module,
                                          @RequestParam("action") String action,
                                          @RequestParam("apikey") String apikey);
}
