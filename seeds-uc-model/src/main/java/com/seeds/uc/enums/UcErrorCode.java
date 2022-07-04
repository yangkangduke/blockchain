package com.seeds.uc.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * @author allen
 * @email allen.hua.ai@gmail.com
 * @date 2020/7/26
 */
@Getter
public enum UcErrorCode {
    ERR_401_NOT_LOGGED_IN(401, "请先登录", "please login first"),
    ERR_500_SYSTEM_BUSY(500, "系统繁忙，请稍后再试...", "system busy, please try again later"),
    ERR_502_ILLEGAL_ARGUMENTS(502, "参数错误", "wrong arguments"),
    ERR_504_MISSING_ARGUMENTS(504, "缺少参数", "missing arguments"),

    // login
    ERR_10001_ACCOUNT_YET_NOT_REGISTERED(10001, "账号尚未注册", "account yet not registered"),
    ERR_10002_INCOMPLETE_USER_INFO(10002, "用户信息不完整，请联系客服", "user info not complete, please contact customer service"),
    ERR_10005_SECURITY_STRATEGY_NOT_SET(10005, "您尚未设置安全策略", "you haven not set security strategy"),
    ERR_10013_ACCOUNT_NAME_PASSWORD_INCORRECT(10013, "账号或密码错误", "account name or password incorrect"),
    ERR_10014_ACCOUNT_FROZEN(10014, "您的账户存在异常，已被锁定，请联系客服人员解决", "account has been locked please contact customer service"),
    ERR_10015_ACCOUNT_FROZEN_MAX_ATTEMPTS_EXCEEDED(10015, "登录错误超过10次,账户被锁定,请2小时后再尝试", "account has been locked for exceeding max attempts"),
    ERR_10019_ACCOUNT_IN_HIBERNATION(10019, "因为您长期未访问，所以已转至休眠账户。如需使用请在PC网页登录并解除休眠状态。", "account in hibernation status, please unlock by login on PC"),
    ERR_10021_PHONE_NUMBER_INCORRECT(10021, "手机号不正确，请重新输入", "phone number incorrect"),
    ERR_10022_EMAIL_INCORRECT(10022, "邮箱不正确，请重新输入", "email incorrect"),

    // registration codes
    ERR_10026_PHONE_EMAIL_CANNOT_BOTH_EMPTY(10026, "手机和邮箱不能同时为空", "phone and email cannot all be empty"),
    ERR_10027_CHOOSE_PHONE_OR_EMAIL(10027, "请选择手机或者邮箱注册", "please choose phone or email to register"),
    ERR_10028_FAILED_DUE_TO_SECURITY_REASONS(10028, "注册由于安全原因失败，请重试", "registration failed due to security reasons"),
    ERR_10032_WRONG_SMS_CODE(10032, "短信验证码不正确，请重新输入", "wrong sms code"),
    ERR_10033_WRONG_EMAIL_CODE(10033, "邮箱验证码不正确，请重新输入", "wrong email code"),
    ERR_10034_SMS_CODE_EXPIRED(10034, "短信验证码已过期，请重新获取", "sms code expired"),
    ERR_10035_SMS_CODE_WRONG_TOO_MANY_TIMES(10035, "短信验证码错误次数过多", "sms code wrong too many times"),
    ERR_10036_AUTH_CODE_EXPIRED(10036, "验证码已过期,请重新获取", "auth code expired"),
    ERR_10037_AUTH_CODE_WRONG_TOO_MANY_TIMES(10037, "验证码错误次数过多", "auth code wrong too many times"),
    ERR_10039_WRONG_GRAPHIC_AUTH_CODE(10039, "图形验证码错误", "graphic auth code wrong"),
    ERR_10043_WRONG_OLD_PASSWORD(10043, "原密码错误", "wrong old password"),
    ERR_10046_SAME_PASSWORD(10046, "新旧密码相同", "old and new password are the same"),
    ERR_10047_LOW_PASSWORD_SEC_LEVEL(10047, "密码安全等级较低", "low password security level"),
    ERR_10048_MALFORMED_PASSWORD(10048, "密码不符合sha256要求，请重新输入密码", "malformed password, please re-enter"),
    ERR_10051_PHONE_ALREADY_BEEN_USED(10051, "手机号已被使用", "phone number already been used"),
    ERR_10053_PHONE_NOT_BIND(10053, "用户未绑定手机", "client have not bind phone"),
    ERR_10061_EMAIL_ALREADY_BEEN_USED(10061, "邮箱用户已存在，请更换邮箱", "email already existed, please change it"),
    ERR_10070_PLEASE_ENTER_2FA(10070, "请输入二次验证项", "please enter 2FA"),
    ERR_10074_2FA_MAX_ATTEMPTS_EXCEEDED(10074, "二次验证错误次数超过6次，暂被冻结", "2FA wrong for too many times"),
    ERR_10075_LOGIN_RISK_DETECTED(10075, "系统检测到此次登录存在安全风险，无法登录，请稍后再试", "system detecting risk on this login, please try again later"),
    ERR_10077_NEED_SMS_AUTH_FOR_UNKNOWN_DEVICE_LOGIN(10077, "您正在尝试从一台未知的设备登录，请通过短信进行设备验证", "you are trying to login on an unknown device, please validate via sms"),
    ERR_10078_NEED_EMAIL_AUTH_FOR_UNKNOWN_DEVICE_LOGIN(10078, "您正在尝试从一台未知的设备登录，请通过邮件进行设备验证", "you are trying to login on an unknown device, please validate via email"),
    ERR_10088_WRONG_GOOGLE_AUTHENTICATOR_CODE(10088, "谷歌验证码错误", "wrong google authenticator code"),
    ERR_10096_GOOGLE_AUTHENTICATOR_CODE_EXPIRED(10096, "谷歌验证码已过期", "google authenticator code expired"),
    ERR_10102_GOOGLE_AUTHENTICATOR_NOT_BIND(10102, "谷歌验证码未绑定", "google authenticator not bind"),
    ERR_10210_SECURITY_VERIFY_ERROR(10210, "安全验证错误", "security verify error"),
    ERR_11114_SECURITY_ITEM_CANNOT_BE_EMPTY(11114, "修改安全策略：验证项不能为空", "security setting item cannot be empty"),
    ERR_11120_SECURITY_ITEM_ENABLED_ALREADY(11120, "该验证项已启用, 请勿重复操作", "this security item has been enabled already"),
    ERR_11121_SECURITY_ITEM_DISABLED_ALREADY(11121, "该验证项已关闭, 请勿重复操作", "this security item has been disabled already"),

    // invitations
    ERR_11501_INVITATION_CODE_NOT_EXIST(11501, "邀请码不存在", "invitation not exist"),
    ERR_11504_INVITATION_CODE_MISSPELLED(10504, "邀请码拼写错误", "invitation code misspelled"),

    // KYC
    ERR_12000_KYC_ALREADY_EXIST(12000, "KYC已存在", "KYC already exist"),
    ;

    @JsonValue
    private Integer code;
    private String desc;
    private String descEn;

    UcErrorCode(Integer code, String desc, String descEn) {
        this.code = code;
        this.desc = desc;
        this.descEn = descEn;
    }
}