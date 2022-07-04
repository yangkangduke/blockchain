package com.seeds.uc.service;

import com.seeds.common.enums.Language;

/**
 * @author allen
 * @email allen.hua.ai@gmail.com
 * @date 2020/9/2
 */
public interface UserLanguageService {
    Language getUserLanguageByUid(Long uid);

    Language getLanguage();
}
