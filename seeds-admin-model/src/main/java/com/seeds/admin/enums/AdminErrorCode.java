package com.seeds.admin.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * @author hang.yu
 * @date 2022/7/13
 */
@Getter
public enum AdminErrorCode {

    ERR_401_NOT_LOGGED_IN(401, "请先登录", "please login first"),
    ERR_500_SYSTEM_BUSY(500, "系统繁忙，请稍后再试...", "system busy, please try again later"),
    ERR_502_ILLEGAL_ARGUMENTS(502, "参数错误", "wrong arguments"),
    ERR_504_MISSING_ARGUMENTS(504, "缺少参数", "missing arguments"),

    // login
    ERR_10001_ACCOUNT_YET_NOT_REGISTERED(10001, "账号尚未注册", "account yet not registered"),
    ERR_10002_INCOMPLETE_USER_INFO(10002, "用户信息不完整，请联系客服", "user info not complete, please contact customer service"),
    ERR_10013_ACCOUNT_NAME_PASSWORD_INCORRECT(10013, "账号或密码错误", "account name or password incorrect"),
    ERR_10014_ACCOUNT_FROZEN(10014, "您的账户已被停用，请联系管理员", "account has been disabled please contact admin"),
    ERR_10015_ACCOUNT_FROZEN_MAX_ATTEMPTS_EXCEEDED(10015, "登录错误超过10次,账户被锁定,请2小时后再尝试", "account has been locked for exceeding max attempts"),
    ERR_10019_ACCOUNT_IN_HIBERNATION(10019, "因为您长期未访问，所以已转至休眠账户。如需使用请在PC网页登录并解除休眠状态。", "account in hibernation status, please unlock by login on PC"),
    ERR_10021_PHONE_NUMBER_INCORRECT(10021, "手机号不正确，请重新输入", "phone number incorrect"),
    ERR_10022_EMAIL_INCORRECT(10022, "邮箱不正确，请重新输入", "email incorrect"),
    ERR_10032_WRONG_SMS_CODE(10032, "短信验证码不正确，请重新输入", "wrong sms code"),
    ERR_10039_WRONG_GRAPHIC_AUTH_CODE(10039, "图形验证码错误", "graphic auth code wrong"),
    ERR_10051_PHONE_ALREADY_BEEN_USED(10051, "手机号已被使用", "phone number already been used"),
    ERR_10061_ACCOUNT_ALREADY_BEEN_USED(10061, "账号已存在，请更换账号", "account already existed, please change it"),
    ERR_10043_WRONG_OLD_PASSWORD(10043, "原密码错误", "wrong old password"),

    // role
    ERR_20001_ROLE_ALREADY_EXIST(20001, "角色已存在", "Role name already exist"),
    ERR_20002_USER_ROLE_ALREADY_EXIST(20002, "该角色当前用户已存在", "The role already exists for the current user"),

    // menu
    ERR_30001_MENU_ALREADY_EXIST(30001, "菜单已存在", "menu code already exist"),
    ERR_30002_MENU_PARENT_ITSELF(30002, "上级菜单不能为自身", "Parent menu cannot be for itself"),
    ERR_30003_SUB_MENU_EXIST(30003, "存在下级菜单", "Subordinate menus exist"),

    // nft type
    ERR_40001_NFT_TYPE_ALREADY_EXIST(40001, "NFT类别已存在", "NFT type code already exist"),
    ERR_40002_NFT_TYPE_PARENT_ITSELF(40002, "上级NFT类别不能为自身", "Parent nft type cannot be for itself"),

    ;

    @JsonValue
    @EnumValue
    private Integer code;
    private String desc;
    private String descEn;

    AdminErrorCode(Integer code, String desc, String descEn) {
        this.code = code;
        this.desc = desc;
        this.descEn = descEn;
    }
}