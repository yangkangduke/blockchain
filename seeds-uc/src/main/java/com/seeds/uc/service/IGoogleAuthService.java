package com.seeds.uc.service;

import javax.servlet.http.HttpServletRequest;

/**
 * @author yk
 * @email 819628513@qq.com
 * @date 2022/07/15
 */
public interface IGoogleAuthService {

    String getQRBarcode(String account, String remark, HttpServletRequest request);

    String genGaSecret();

    boolean verifyUserCode(Long uid, String userInputCode);

    boolean verify(String userInputCode, String userSecret);
}