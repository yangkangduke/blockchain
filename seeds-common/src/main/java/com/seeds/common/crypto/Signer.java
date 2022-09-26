package com.seeds.common.crypto;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * @author rickierao
 * @email antilaw@yahoo.com
 * @date 2021/1/21
 */

/**
 * The HmacSHA256 signer that creates the signature by message
 */
public class Signer {
    private static final String HMAC_SHA256 = "HmacSHA256";

    private final String secretKey;

    public Signer(String secretKey) {
        this.secretKey = secretKey;
    }

    public String createSignature(byte[] message) {
        byte[] digest = calculateDigest(message);
        return Base64.getUrlEncoder().encodeToString(digest);
    }

    private byte[] calculateDigest(byte[] message) {
        try {
            byte[] secretKeyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
            SecretKeySpec keySpec = new SecretKeySpec(secretKeyBytes, HMAC_SHA256);
            Mac mac = Mac.getInstance(HMAC_SHA256);
            mac.init(keySpec);
            mac.update(message);
            return mac.doFinal();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("Invalid MAC algorithm: " + HMAC_SHA256, e);
        } catch (InvalidKeyException e) {
            throw new IllegalStateException("Invalid MAC secret key", e);
        }
    }
}
