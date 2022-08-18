package com.seeds.chain.service.impl;

import com.seeds.chain.config.SmartContractConfig;
import com.seeds.chain.contracts.GameItems;
import com.seeds.chain.service.GameItemsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.FastRawTransactionManager;
import org.web3j.tx.gas.DefaultGasProvider;

import javax.annotation.PostConstruct;
import java.math.BigInteger;

@Slf4j
@Service
public class GameItemsServiceImpl implements GameItemsService {

    @Autowired
    private SmartContractConfig smartContractConfig;
    private GameItems gameItems;
    private Web3j web3;
    private FastRawTransactionManager txManager;

    @Override
    public String getUri(BigInteger tokenId) {
        gameItems = GameItems.load(smartContractConfig.getGameAddress(), web3, txManager, new DefaultGasProvider());
        try {
            return gameItems.uri(tokenId).send();
        } catch (Exception e) {
            log.error("fail to get result");
        }
        return "";
    }

    @Override
    public boolean mintNft(String uri) {
        gameItems = GameItems.load(smartContractConfig.getGameAddress(), web3, txManager, new DefaultGasProvider());
        try {
            TransactionReceipt receipt = gameItems.mintNewNft(smartContractConfig.getOwnerAddress(), BigInteger.ONE, uri).send();
            log.info("receipt: {}", receipt);
            return true;
        } catch (Exception e) {
            log.error("fail to mint", e);
        }
        return false;
    }

    @Override
    public boolean updateNftAttribute(BigInteger tokenId, String newUri) {
        try {
            TransactionReceipt receipt = gameItems.setTokenURI(tokenId, newUri).send();
            log.info("receipt: {}", receipt);
            return true;
        } catch (Exception e) {
            log.error("fail to update");
        }
        return false;
    }

    @Override
    public boolean burnNft(String tokenId) {
        gameItems = GameItems.load(smartContractConfig.getGameAddress(), web3, txManager, new DefaultGasProvider());
        try {
            TransactionReceipt receipt = gameItems.burn(smartContractConfig.getOwnerAddress(), new BigInteger(tokenId), BigInteger.ONE).send();
            log.info("receipt: {}", receipt);
            return true;
        } catch (Exception e) {
            log.error("fail to update");
        }
        return false;
    }

    @PostConstruct
    public void init() {
        web3 = Web3j.build(new HttpService(smartContractConfig.getRpcProviderUrl()));
        Credentials credentials = Credentials.create(smartContractConfig.getOwnerPrivateKey());
        txManager = new FastRawTransactionManager(web3, credentials, smartContractConfig.getChainId());
    }

}
