package com.seeds.uc.util;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PasswordUtil {
    private PasswordUtil() {
    }


    // 存库的password = sha256(sha256(password) + salt)
    public static String getPassword(String password, String salt) {
        return HashUtil.sha256(password + salt);
    }
}
