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
    // 忘记密码
    public static final String FORGOT_PASSWORD_EMAIL_CONTENT = "Reset password address, it will expire in 10 minutes：";
    public static final String FORGOT_PASSWORD_EMAIL_SUBJECT = "forgot password";
    public static final String FORGOT_PASSWORD_EMAIL_LINK_SYMBOL = ";";

    // 注册邮箱账号
    public static final String REGISTER_EMAIL_SUBJECT = "Register an account";
    public static final String REGISTER_EMAIL_CONTENT = "Register the email verification code, note that it expires in 5 minutes:";

    // 绑定邮箱
    public static final String BIND_EMAIL_SUBJECT = "Bind the email verification code";
    public static final String BIND_EMAIL_CONTENT = "Bind the email verification code, note that it expires in 5 minutes:";

    // 登陆2fa
    public static final String LOGIN_EMAIL_VERIFY_SUBJECT = "2fa verification";
    public static final String LOGIN_EMAIL_VERIFY_CONTENT = "2fa verification，note that it expires in 5 minutes:";
}