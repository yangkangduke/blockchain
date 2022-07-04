package com.seeds.uc.service;

import com.seeds.uc.dto.UserDto;
import com.seeds.uc.enums.ClientAuthTypeEnum;

/**
 * @author allen
 * @email allen.hua.ai@gmail.com
 * @date 2020/8/22
 */
public interface RiskControlService {
    // after login success, check if need 2FA
    boolean checkNeed2FA(UserDto userDto);

    // 检查用户使用了哪个安全项
    ClientAuthTypeEnum getUserAuthTypeEnum(UserDto userDto);
}
