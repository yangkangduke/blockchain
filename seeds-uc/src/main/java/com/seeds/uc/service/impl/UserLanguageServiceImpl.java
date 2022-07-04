package com.seeds.uc.service.impl;

import com.seeds.common.enums.Language;
import com.seeds.uc.dto.UserDto;
import com.seeds.uc.mapper.CountryLanguageMapper;
import com.seeds.uc.mapper.UserMapper;
import com.seeds.uc.model.CountryLanguage;
import com.seeds.uc.service.UserLanguageService;
import com.seeds.uc.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author allen
 * @email allen.hua.ai@gmail.com
 * @date 2020/9/2
 */
@Service
public class UserLanguageServiceImpl implements UserLanguageService {
    @Autowired
    UserMapper userMapper;

    @Autowired
    UserService userService;

    @Autowired
    CountryLanguageMapper countryLanguageMapper;

    @Override
    public Language getUserLanguageByUid(Long uid) {
        UserDto userDto = userService.getUserByUid(uid);
        // 看用户有没有country code，没有的话看kyc
        CountryLanguage countryLanguage = null;
        if (StringUtils.isNotBlank(userDto.getNationality())) {
            countryLanguage = countryLanguageMapper.selectByCountryCode(userDto.getNationality());
        }else if (StringUtils.isNotBlank(userDto.getCountryCode())){
            countryLanguage = countryLanguageMapper.selectByCountryCode(userDto.getCountryCode());
        }

        if (countryLanguage != null) {
            return countryLanguage.getLanguage();
        } else {
            // 登陆地也拿不到的时候，用英文
            return Language.DEFAULT;
        }
        // TODO  从kyc里拿国籍

        // TODO 当都拿不到的时候，拿最后一次登陆地的语言

    }

    @Override
    public Language getLanguage() {
        return Language.DEFAULT;
    }
}
