package com.seeds.uc.constant;

import lombok.experimental.UtilityClass;

/**
 * uc相关的常量
 *
 * @author yk
 * @email 819628513@qq.com
 * @date 2022/07/13
 */
@UtilityClass
public class UcConstant {
    public static final String GA_ISSUER = "dev.seeds.com";

    // 忘记密码
    public static final String FORGOT_PASSWORD_EMAIL_SUBJECT = "Reset password";
    public static final String FORGOT_PASSWORD_EMAIL_CONTENT = "Reset password verification code, note that it expires in {} minutes:{}";

    // 重置GA
    public static final String RESET_GA_EMAIL_SUBJECT = "Reset ga";
    public static final String RESET_GA_EMAIL_CONTENT = "Reset ga verification code, note that it expires in {} minutes:{}";

    // 注册邮箱账号
    public static final String REGISTER_EMAIL_SUBJECT = "Register an account";
    public static final String REGISTER_EMAIL_CONTENT = "Register the email verification code, note that it expires in {} minutes:{}";

    // 登陆2fa
    public static final String LOGIN_EMAIL_VERIFY_SUBJECT = "2fa verification";
    public static final String LOGIN_EMAIL_VERIFY_CONTENT = "2fa verification，note that it expires in {} minutes:{}";

    // 修改密码
    public static final String CHANGE_PASSWORD_EMAIL_SUBJECT = "change Password";
    public static final String CHANGE_PASSWORD_EMAIL_CONTENT = "change Password verification code, note that it expires in {} minutes:{}";

    // 修改邮箱
    public static final String CHANGE_EMAIL_EMAIL_SUBJECT = "change email";
    public static final String CHANGE_EMAIL_EMAIL_CONTENT = "change email verification code, note that it expires in {} minutes:{}";

    // 绑定邮箱
    public static final String BIND_EMAIL_EMAIL_SUBJECT = "bind email";
    public static final String BIND_EMAIL_EMAIL_CONTENT = "bind email verification code, note that it expires in {} minutes:{}";

}