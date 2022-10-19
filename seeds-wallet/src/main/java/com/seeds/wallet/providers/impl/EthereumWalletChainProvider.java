package com.seeds.wallet.providers.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.seeds.common.enums.Chain;
import com.seeds.wallet.dto.RawTransactionDto;
import com.seeds.wallet.dto.SignedMessageDto;
import com.seeds.wallet.mapper.HotWalletMapper;
import com.seeds.wallet.model.HotWallet;
import com.seeds.wallet.providers.WalletChainProvider;
import com.seeds.wallet.service.WalletService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.web3j.abi.DefaultFunctionEncoder;
import org.web3j.abi.datatypes.generated.Bytes32;
import org.web3j.abi.datatypes.generated.Uint8;
import org.web3j.crypto.*;
import org.web3j.utils.Numeric;

import javax.annotation.PostConstruct;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 *  支持ETH和BSC，如果传入的是BSC就转化成ETH链
 *
 *  ETH和BSC使用相同方式处理，共享地址，但是chainId不同
 *
 * @author yk
 *
 *
 */
@Slf4j
@Component
@Transactional
public class EthereumWalletChainProvider implements WalletChainProvider {

    @Value("${wallet.passwordKey}")
    private String password;

    @Value("${wallet.chainId}")
    private long chainId;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private HotWalletMapper hotWalletMapper;

    @Autowired
    private DefaultFunctionEncoder functionEncoder;

    @Autowired
    WalletService walletService;

    @PostConstruct
    public void init() {
        walletService.registerProvider(Chain.ETH.getCode(), this);
        walletService.registerProvider(Chain.BSC.getCode(), this);
        walletService.registerProvider(Chain.OKC.getCode(), this);
        walletService.registerProvider(Chain.HECO.getCode(), this);
        walletService.registerProvider(Chain.MATIC.getCode(), this);
    }

    @Override
    @Transactional
    public String createNewWallet(int chain) throws Exception {
        chain = mapChain(chain);

        ECKeyPair ecKeyPair = Keys.createEcKeyPair();
        org.web3j.crypto.WalletFile walletFile = Wallet.createStandard(password, ecKeyPair);
        String walletFileJson = objectMapper.writeValueAsString(walletFile);

        hotWalletMapper.insert(HotWallet.builder()
                .chain(chain)
                .address(walletFile.getAddress())
                .fileJson(walletFileJson)
                .createdAt(System.currentTimeMillis())
                .build());
        return Numeric.prependHexPrefix(walletFile.getAddress());
    }

    @Override
    @Transactional
    public String signRawTransaction(int chain, long chainId, RawTransactionDto rawTransaction, String address) throws Exception {
        chain = mapChain(chain);

        if (chainId == 0) {
            chainId = this.chainId;
        }

        address = Numeric.cleanHexPrefix(address);
        HotWallet hotWallet = hotWalletMapper.selectByAddress(chain, address);
        if (hotWallet == null) {
            throw new RuntimeException("invalid address");
        }
        Credentials credentials = WalletUtils.loadJsonCredentials(password, hotWallet.getFileJson());
        RawTransaction rawT = RawTransaction.createTransaction(
                rawTransaction.getNonce(),
                rawTransaction.getGasPrice(),
                rawTransaction.getGasLimit(),
                rawTransaction.getTo(),
                rawTransaction.getValue(),
                rawTransaction.getData(),
                rawTransaction.getGasPremium(),
                rawTransaction.getFeeCap()
        );
        byte[] signedBytes = TransactionEncoder.signMessage(rawT, chainId, credentials);
        String signedMessage = Numeric.toHexString(signedBytes);
        return signedMessage;
    }

    @Override
    public SignedMessageDto signMessage(int chain, String messageToSign, String address) throws Exception {
        chain = mapChain(chain);

        HotWallet hotWallet = hotWalletMapper.selectByAddress(chain, address);
        if (hotWallet == null) {
            throw new RuntimeException("invalid address");
        }
        // Load the credentials
        Credentials credentials = WalletUtils.loadJsonCredentials(password, hotWallet.getFileJson());
        String tmpHash = Hash.sha3(messageToSign);

        // Sign it with the prefix: "\u0019Ethereum Signed Message:\n"
        Sign.SignatureData sig = Sign.signPrefixedMessage(Numeric.hexStringToByteArray(tmpHash), credentials.getEcKeyPair());

        String signature =
                functionEncoder.encodeParameters(
                        Arrays.asList(new Bytes32(sig.getR()), new Bytes32(sig.getS()), new Uint8(new BigInteger(sig.getV()))));

        return new SignedMessageDto(tmpHash, messageToSign, signature, credentials.getAddress());
    }

    @Override
    public List<SignedMessageDto> signMessages(int chain, List<String> messagesToSign, String address) throws Exception {
        chain = mapChain(chain);

        address = Numeric.cleanHexPrefix(address);

        List<SignedMessageDto> list = new LinkedList<>();
        for (String m : messagesToSign) {
            SignedMessageDto signedMessageDto = signMessage(chain, m, address);
            list.add(signedMessageDto);
        }
        return list;
    }

    private int mapChain(int chain) {
        return Chain.mapChain(Chain.fromCode(chain)).getCode();
    }
}
