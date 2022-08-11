package com.seeds.uc.util;

import org.web3j.crypto.*;
import org.web3j.crypto.Sign.SignatureData;
import org.web3j.utils.Numeric;

import java.math.BigInteger;
import java.util.Arrays;

/**
 * 以太坊签名消息校验工具
 */
public class CryptoUtils {

    public static void main(String[] args) {
        String signature = "0x53ea88d24f4ef8cdcc4bcc843912510b065cd6014c453ff61316c4cd75162f0a38f83a2103da028fb8e5181292ba194b0c8aa21a9ddacdf6783ebfa608889d121c";
        String message = "Hello Dapp";
        String publicAddress = "0x40141cf4756a72df8d8f81c1e0c2ad403c127b9e";

        // 地址合法性校验
        if (!WalletUtils.isValidAddress(publicAddress)) {
            // 不合法直接返回错误
             System.out.println("地址格式非法！");
        }

        // 校验签名信息
        if (!CryptoUtils.validate(signature, message, publicAddress)) {
            System.out.println("签名校验失败！");
        } else {
            System.out.println("成功！");
        }
    }

    /**
     * 以太坊自定义的签名消息都以以下字符开头
     * 参考 eth_sign in https://github.com/ethereum/wiki/wiki/JSON-RPC
     */
    public static final String PERSONAL_MESSAGE_PREFIX = "\u0019Ethereum Signed Message:\n";

    /**
     * 对签名消息，原始消息，账号地址三项信息进行认证，判断签名是否有效
     *
     * @param signature
     * @param message
     * @param address
     * @return
     */
    public static boolean validate(String signature, String message, String address) {
        //参考 eth_sign in https://github.com/ethereum/wiki/wiki/JSON-RPC
        // eth_sign
        // The sign method calculates an Ethereum specific signature with:
        //    sign(keccak256("\x19Ethereum Signed Message:\n" + len(message) + message))).
        //
        // By adding a prefix to the message makes the calculated signature recognisable as an Ethereum specific signature.
        // This prevents misuse where a malicious DApp can sign arbitrary data (e.g. transaction) and use the signature to
        // impersonate the victim.
        String prefix = PERSONAL_MESSAGE_PREFIX + message.length();
        byte[] msgHash = Hash.sha3((prefix + message).getBytes());

        byte[] signatureBytes = Numeric.hexStringToByteArray(signature);
        byte v = signatureBytes[64];
        if (v < 27) {
            v += 27;
        }

        SignatureData sd = new SignatureData(
                v,
                Arrays.copyOfRange(signatureBytes, 0, 32),
                Arrays.copyOfRange(signatureBytes, 32, 64));

        String addressRecovered = null;
        boolean match = false;

        // Iterate for each possible key to recover
        for (int i = 0; i < 4; i++) {
            BigInteger publicKey = Sign.recoverFromSignature(
                    (byte) i,
                    new ECDSASignature(new BigInteger(1, sd.getR()), new BigInteger(1, sd.getS())),
                    msgHash);

            if (publicKey != null) {
                addressRecovered = "0x" + Keys.getAddress(publicKey);

                if (addressRecovered.equals(address)) {
                    match = true;
                    break;
                }
            }
        }
        return match;
    }
}


