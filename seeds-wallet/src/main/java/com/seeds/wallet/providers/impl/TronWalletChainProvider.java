package com.seeds.wallet.providers.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.protobuf.ByteString;
import com.seeds.wallet.dto.RawTransactionDto;
import com.seeds.wallet.dto.SignedMessageDto;
import com.seeds.wallet.mapper.HotWalletMapper;
import com.seeds.wallet.model.HotWallet;
import com.seeds.wallet.providers.WalletChainProvider;
import com.seeds.wallet.service.WalletService;
import com.seeds.wallet.tron.TronDESUtils;
import com.seeds.wallet.tron.TronWalletFile;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.jcajce.provider.digest.Keccak;
import org.bouncycastle.jcajce.provider.digest.SHA256;
import org.bouncycastle.util.encoders.Hex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.tron.trident.crypto.SECP256K1;
import org.tron.trident.crypto.tuwenitypes.Bytes;
import org.tron.trident.crypto.tuwenitypes.Bytes32;
import org.tron.trident.proto.Chain;
import org.tron.trident.proto.Response;
import org.tron.trident.utils.Base58Check;

import javax.annotation.PostConstruct;
import java.util.Base64;
import java.util.List;


/**
 * @author milo
 *
 * createTransaction -> toByteArray -> encodeToBase64String (kine-account-service)
 * decodeBase64String -> parseToTransaction -> sign -> toByteArray -> encodeToBase64String (kine-wallet-service)
 *
 */
@Slf4j
@Component
@Transactional
public class TronWalletChainProvider implements WalletChainProvider {

    @Value("${wallet.passwordKey}")
    private String password;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private HotWalletMapper hotWalletMapper;

    @Autowired
    WalletService walletService;

    @PostConstruct
    public void init() {
        walletService.registerProvider(com.seeds.common.enums.Chain.TRON.getCode(), this);
    }

    @Override
    public String createNewWallet(int chain) throws Exception {
        TronWalletFile walletFile = generateTronWallet();
        String encrypted = TronDESUtils.encrypt(password, objectMapper.writeValueAsString(walletFile));

        hotWalletMapper.insert(HotWallet.builder()
                .chain(chain)
                .address(walletFile.getAddress())
                .fileJson(encrypted)
                .createdAt(System.currentTimeMillis())
                .build());
        return walletFile.getAddress();
    }

    @Override
    public String signRawTransaction(int chain, long chainId, RawTransactionDto rawTransaction, String address) throws Exception {
        // 如果是TransactionExtension
        if (rawTransaction.getTronTransactionExtensionBase64() != null && rawTransaction.getTronTransactionExtensionBase64().length() > 0) {
            return signTransactionExtension(rawTransaction.getTronTransactionExtensionBase64(), address);
        }
        // 如果是Transaction
        if (rawTransaction.getTronTransactionBase64() != null && rawTransaction.getTronTransactionBase64().length() > 0) {
            return signTransaction(rawTransaction.getTronTransactionBase64(), address);
        }
        return null;
    }

    @Override
    public SignedMessageDto signMessage(int chain, String messageToSign, String address) throws Exception {
        throw new RuntimeException("not implemented");
    }

    @Override
    public List<SignedMessageDto> signMessages(int chain, List<String> messageToSign, String address) throws Exception {
        throw new RuntimeException("not implemented");
    }

    public String signTransactionExtension(String txnBase64, String address) throws Exception {
        // 1. decodeBase64String
        byte[] sourceData = Base64.getDecoder().decode(txnBase64);
        // 2. parseFrom
        Response.TransactionExtention txn = Response.TransactionExtention.parseFrom(sourceData);
        // 3. signTransaction
        Chain.Transaction signedTxn = signTransactionExtension(txn, address);
        // 4. toByteArray
        byte[] signedData = signedTxn.toByteArray();
        // 5. encodeToString
        return Base64.getEncoder().encodeToString(signedData);
    }

    public String signTransaction(String txnBase64, String address) throws Exception {
        // 1. decodeBase64String
        byte[] sourceData = Base64.getDecoder().decode(txnBase64);
        // 2. parseFrom
        Chain.Transaction txn = Chain.Transaction.parseFrom(sourceData);
        // 3. signTransaction
        Chain.Transaction signedTxn = signTransaction(txn, address);
        // 4. toByteArray
        byte[] signedData = signedTxn.toByteArray();
        // 5. encodeToString
        return Base64.getEncoder().encodeToString(signedData);
    }

    public Chain.Transaction signTransaction(Chain.Transaction txn, String address) throws Exception {
        SECP256K1.KeyPair kp = toKey(address);
        SHA256.Digest digest = new SHA256.Digest();
        digest.update(txn.getRawData().toByteArray());
        byte[] txid = digest.digest();
        SECP256K1.Signature sig = SECP256K1.sign(Bytes32.wrap(txid), kp);
        Chain.Transaction signedTxn = txn.toBuilder().addSignature(ByteString.copyFrom(sig.encodedBytes().toArray())).build();
        return signedTxn;
    }

    public Chain.Transaction signTransactionExtension(Response.TransactionExtention txnExt, String address) throws Exception {
        SECP256K1.KeyPair kp = toKey(address);
        SECP256K1.Signature sig = SECP256K1.sign(Bytes32.wrap(txnExt.getTxid().toByteArray()), kp);
        Chain.Transaction signedTxn = txnExt.getTransaction().toBuilder().addSignature(ByteString.copyFrom(sig.encodedBytes().toArray())).build();
        return signedTxn;
    }

    /**
     * 获取地址对应的私钥
     * @param address
     * @return
     */
    private SECP256K1.KeyPair toKey(String address) throws Exception {
        HotWallet hotWallet = hotWalletMapper.selectByAddress(com.seeds.common.enums.Chain.TRON.getCode(), address);
        if (hotWallet == null) {
            throw new RuntimeException("invalid address");
        }
        String decrypted = TronDESUtils.decrypt(password, hotWallet.getFileJson());
        TronWalletFile walletFile = objectMapper.readValue(decrypted, TronWalletFile.class);

        SECP256K1.PrivateKey privateKey = SECP256K1.PrivateKey.create(Bytes32.fromHexString(walletFile.getPrivateKey()));
        SECP256K1.PublicKey publicKey = SECP256K1.PublicKey.create(Bytes.wrap(Hex.decode(walletFile.getPublicKey())));
        SECP256K1.KeyPair kp = new SECP256K1.KeyPair(privateKey, publicKey);
        return kp;
    }

    /**
     * Address Format
     * Use the public key P as the input, and use SHA3 get the result H. The length of the public key is 64 bytes (SHA3 uses Keccak256). Use the last 20 bytes of H, and add a byte of 0x41 as a prefix. Do a basecheck (see next paragraph), and the result will be the final address. All addresses start with 'T'.
     *
     * Basecheck process: first run SHA256 on the address to get h1, then run SHA256 on h1 to get h2. Use the first 4 bytes as a checksum, add it to the end of the address (address||check). Finally, base58 encode address||check to get the final result.
     * @return
     */
    private TronWalletFile generateTronWallet() {
        SECP256K1.KeyPair kp = SECP256K1.KeyPair.generate();
        SECP256K1.PrivateKey privateKey = kp.getPrivateKey();
        SECP256K1.PublicKey publicKey = kp.getPublicKey();

        Keccak.Digest256 digest = new Keccak.Digest256();
        digest.update(publicKey.getEncoded(), 0, 64);
        byte[] raw = digest.digest();
        byte[] address = new byte[21];
        address[0] = 0x41;
        System.arraycopy(raw, 12, address, 1, 20);

        return TronWalletFile.builder()
                /*  私钥 */
                .privateKey(Hex.toHexString(privateKey.getEncoded()))
                /* 公钥 */
                .publicKey(Hex.toHexString(publicKey.getEncoded()))
                /* 地址 */
                .address(Base58Check.bytesToBase58(address))
                .build();
    }
}
