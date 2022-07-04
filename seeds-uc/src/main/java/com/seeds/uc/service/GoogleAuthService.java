package com.seeds.uc.service;

/**
 * @author allen
 * @email allen.hua.ai@gmail.com
 * @date 2020/8/26
 */
public interface GoogleAuthService {
    String genGaSecret();

    boolean verifyUserCode(Long uid, String userInputCode);

    boolean verify(String userInputCode, String userSecret);
}