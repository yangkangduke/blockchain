package com.seeds.admin.utils;

import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

/**
* @author yk
 * @date 2020/7/26
 */
@Slf4j
public class HashUtil {
    private HashUtil() {
    }

    public static String MD5(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            byte[] encodedHash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(encodedHash);

        } catch (Exception e) {
            log.warn("md5 error!", e);
            return null;
        }
    }

    public static String bytesToHex(byte[] hash) {
        StringBuffer hexString = new StringBuffer();
        for (byte c : hash) {
            String hex = Integer.toHexString(0xff & c);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }

    public static String sha1(String str) {
        if (str == null || str.length() == 0) {
            return null;
        }

        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            byte[] encodedHash = digest.digest(str.getBytes(StandardCharsets.UTF_8));

            return bytesToHex(encodedHash);
        } catch (Exception e) {
            log.warn("sha1 error!", e);
            return null;
        }
    }

    public static String sha256(String str) {
        if (str == null || str.length() == 0) {
            return null;
        }

        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedHash = digest.digest(str.getBytes(StandardCharsets.UTF_8));

            return bytesToHex(encodedHash);
        } catch (Exception e) {
            log.warn("sha1 error!", e);
            return null;
        }
    }
}
