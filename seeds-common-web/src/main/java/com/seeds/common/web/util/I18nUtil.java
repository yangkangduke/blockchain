package com.seeds.common.web.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author: he.wei
 * @date 2023/6/19
 */
@Component
public class I18nUtil {

    private static I18nUtil i18nUtil;

    @Autowired
    private MessageSource messageSource;

    @PostConstruct
    public void init() {
        i18nUtil = this;
    }

    public static String getMessage(String code) {
        return i18nUtil.messageSource.getMessage(code, null, LocaleContextHolder.getLocale());
    }
}