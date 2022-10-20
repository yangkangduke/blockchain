package com.seeds.wallet.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.seeds.common.dto.GenericDto;
import com.seeds.common.web.inner.Inner;
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
    private static final ObjectMapper objectMapper = new ObjectMapper();
    public static void main(String[] args) throws JsonProcessingException, CipherException {
        String s = "{\"address\":\"477117d29d41307a980b56565e3f1ab79a1bad12\",\"id\":\"0e483d07-a7f9-472e-a9af-25fb7d011d6f\",\"version\":3,\"crypto\":{\"cipher\":\"aes-128-ctr\",\"ciphertext\":\"2e55ac8b7412bc7e98bce86472cd1174504c7887e56c2f3f6e71a0322d9451ed\",\"cipherparams\":{\"iv\":\"07356cd7597f1f60b6d8da32b2a30dbd\"},\"kdf\":\"scrypt\",\"kdfparams\":{\"dklen\":32,\"n\":262144,\"p\":1,\"r\":8,\"salt\":\"fd0eae6f2eb72c9a5682dbc9dad805209807d9a7a0da1f97352cf8447218d00b\"},\"mac\":\"5e7ea7028762f634d1a77808552536dd4711808ccabf7bce84b7f61b6e815160\"}}";
        ECKeyPair decrypt = Wallet.decrypt("5bb40a4623ead547e4b2f15a", objectMapper.readValue(s , new TypeReference<WalletFile>() {
        }));
        BigInteger privateKey = decrypt.getPrivateKey();
        BigInteger publicKey = decrypt.getPublicKey();
        System.out.println("私钥：" + privateKey);
        System.out.println("公钥匙：" + publicKey);
        System.out.println("地址：" + publicKey);
    }

    @PostMapping("create")
    @Inner
    @ApiOperation(value = "创建", notes = "创建")
    public GenericDto<String> createAccount(@RequestParam("chain") int chain) throws Exception {
        return GenericDto.success(walletService.createNewWallet(chain));
    }

    @PostMapping("sign")
    @ApiOperation(value = "签名", notes = "签名")
    @Inner
    public GenericDto<String> sign( @RequestBody RawTransactionSignRequest request) throws Exception {
        String signedMessage = walletService.signRawTransaction(request.getChain(), request.getChainId(), request.getRawTransaction(), request.getFromAddress());
        return GenericDto.success(signedMessage);
    }

    @PostMapping("signMessages")
    @ApiOperation(value = "签名消息", notes = "签名消息")
    @Inner
    public GenericDto<List<SignedMessageDto>> signMessages(@RequestBody SignMessageRequest request) throws Exception {
        return GenericDto.success(walletService.signMessages(request.getChain(), request.getMessagesToSign(), request.getAddress()));
    }

}
