package com.seeds.wallet.controller;

import com.seeds.common.dto.GenericDto;
import com.seeds.wallet.dto.RawTransactionSignRequest;
import com.seeds.wallet.dto.SignMessageRequest;
import com.seeds.wallet.dto.SignedMessageDto;
import com.seeds.wallet.service.WalletService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("wallet")
@Slf4j
public class WalletController {

    @Autowired
    private WalletService walletService;

    @PostMapping("create")
    public GenericDto<String> createAccount(@RequestParam("chain") int chain) throws Exception {
        return GenericDto.success(walletService.createNewWallet(chain));
    }

    @PostMapping("sign")
    public GenericDto<String> sign( @RequestBody RawTransactionSignRequest request) throws Exception {
        String signedMessage = walletService.signRawTransaction(request.getChain(), request.getChainId(), request.getRawTransaction(), request.getFromAddress());
        return GenericDto.success(signedMessage);
    }

    @PostMapping("signMessages")
    public GenericDto<List<SignedMessageDto>> signMessages(@RequestBody SignMessageRequest request) throws Exception {
        return GenericDto.success(walletService.signMessages(request.getChain(), request.getMessagesToSign(), request.getAddress()));
    }

}
