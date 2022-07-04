package com.seeds.uc.util;

import com.seeds.uc.constant.UcTestConstants;
import lombok.extern.slf4j.Slf4j;

import java.security.SecureRandom;
import java.util.UUID;

/**
 * @author allen
 * @email allen.hua.ai@gmail.com
 * @date 2020/7/28
 */
@Slf4j
public class RandomUtil {
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    private RandomUtil() {
    }

    // 24位random salt
    public static String getRandomSalt() {
        byte[] bytes = new byte[12];
        SECURE_RANDOM.nextBytes(bytes);
        return HashUtil.bytesToHex(bytes);
    }

    // random boolean
    public static boolean getRandomBoolean() {
        return SECURE_RANDOM.nextBoolean();
    }

    // get random 6 digits OTP
    public static String getRandom6DigitsOTP() {
//        int otp = SECURE_RANDOM.nextInt(999999);
//        return String.format("%06d", otp);
        return UcTestConstants.TEST_AUTH_CODE;
    }

    // generate uuid
    public static String getUUID() {
        return UUID.randomUUID().toString();
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
