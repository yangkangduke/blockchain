package com.seeds.uc.service;

import com.seeds.uc.dto.LoginUserDTO;

/**
 * @author yk
 * @email 819628513@qq.com
 * @date 2022/07/15
 */
public interface IGoogleAuthService {

    String getQRBarcode(String account, String remark, LoginUserDTO loginUser);

    String genGaSecret();

    void verifyUserCode(Long uid, String userInputCode);

    boolean verify(String userInputCode, String userSecret);
}