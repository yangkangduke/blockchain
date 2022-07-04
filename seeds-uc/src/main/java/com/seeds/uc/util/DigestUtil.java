package com.seeds.uc.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.codec.Hex;
import org.apache.shiro.crypto.AesCipherService;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Arrays;

@Slf4j
public class DigestUtil {
    private static final AesCipherService aesCipherService;
    private static byte[] bytes;

    static {
        String key = "user";
        try {
            bytes = key.getBytes(StandardCharsets.UTF_8);

            bytes = Arrays.copyOf(bytes, 16);
        } catch (Exception ex) {
            bytes = null;
        }

        aesCipherService = new AesCipherService();
        aesCipherService.setKeySize(128); // 设置 key 长度 //生成 key

        Key key1 = aesCipherService.generateNewKey();
        String baseEncrypt = aesCipherService.encrypt(key.getBytes(), key1.getEncoded()).toHex();
        log.info("baseEncrypt is {}", baseEncrypt);
    }

    private DigestUtil() {
    }

    /**
     * 加密
     */
    public static String Encrypt(String value) {
        return aesCipherService.encrypt(value.getBytes(), bytes)
                .toHex();
    }

    /**
     * 解密
     */
    public static String Decrypt(String value) {
        log.info("value: " + value);
        return new String(aesCipherService.decrypt(
                Hex.decode(value), bytes).getBytes());
    }
}
