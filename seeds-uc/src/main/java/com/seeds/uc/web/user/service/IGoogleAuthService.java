package com.seeds.uc.web.user.service;

/**
 * @author yk
 * @email 819628513@qq.com
 * @date 2022/07/15
 */
public interface IGoogleAuthService {
    String genGaSecret();

    boolean verifyUserCode(Long uid, String userInputCode);

    boolean verify(String userInputCode, String userSecret);
}