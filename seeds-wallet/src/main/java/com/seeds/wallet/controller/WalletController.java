package com.seeds.wallet.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.seeds.common.dto.GenericDto;
import com.seeds.wallet.dto.RawTransactionSignRequest;
import com.seeds.wallet.dto.SignMessageRequest;
import com.seeds.wallet.dto.SignedMessageDto;
import com.seeds.wallet.service.WalletService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Wallet;
import org.web3j.crypto.WalletFile;

import java.math.BigInteger;
import java.util.List;

@RestController
@Api(tags = "钱包")
@RequestMapping("wallet")
@Slf4j
public class WalletController {

    @Autowired
    private WalletService walletService;


    @PostMapping("create")
    @ApiOperation(value = "创建", notes = "创建")
    public GenericDto<String> createAccount(@RequestParam("chain") int chain) throws Exception {
        return GenericDto.success(walletService.createNewWallet(chain));
    }

    @PostMapping("sign")
    @ApiOperation(value = "签名", notes = "签名")
    public GenericDto<String> sign( @RequestBody RawTransactionSignRequest request) throws Exception {
        String signedMessage = walletService.signRawTransaction(request.getChain(), request.getChainId(), request.getRawTransaction(), request.getFromAddress());
        return GenericDto.success(signedMessage);
    }

    @PostMapping("signMessages")
    @ApiOperation(value = "签名消息", notes = "签名消息")
    public GenericDto<List<SignedMessageDto>> signMessages(@RequestBody SignMessageRequest request) throws Exception {
        return GenericDto.success(walletService.signMessages(request.getChain(), request.getMessagesToSign(), request.getAddress()));
    }

}
