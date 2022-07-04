package com.seeds.uc.service.impl;

import com.seeds.uc.dto.UserDto;
import com.seeds.uc.enums.ClientAuthTypeEnum;
import com.seeds.uc.service.RiskControlService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author allen
 * @email allen.hua.ai@gmail.com
 * @date 2020/8/22
 */
@Slf4j
@Service
public class RiskControlServiceImpl implements RiskControlService {
    @Override
    public boolean checkNeed2FA(UserDto userDto) {
        return true;
    }

    @Override
    public ClientAuthTypeEnum getUserAuthTypeEnum(UserDto userDto) {
        return userDto.getAuthType();
    }
}
