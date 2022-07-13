package com.seeds.admin.utils;

import lombok.extern.slf4j.Slf4j;

import java.security.SecureRandom;

/**
 * @author allen
 * @email allen.hua.ai@gmail.com
 * @date 2020/7/28
 */
@Slf4j
public class RandomUtil {
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    // 24位random salt
    public static String getRandomSalt() {
        byte[] bytes = new byte[12];
        SECURE_RANDOM.nextBytes(bytes);
        return HashUtil.bytesToHex(bytes);
    }

    public static String genRandomToken(String input) {
        String preHash = input + System.currentTimeMillis() + RandomUtil.getRandomSnippetString();
        return HashUtil.sha1(preHash);
    }

    // 6位random string
    public static String getRandomSnippetString() {
        byte[] bytes = new byte[3];
        SECURE_RANDOM.nextBytes(bytes);
        return HashUtil.bytesToHex(bytes);
    }


}
