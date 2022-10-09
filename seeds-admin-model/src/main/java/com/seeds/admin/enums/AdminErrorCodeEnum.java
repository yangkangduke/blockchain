package com.seeds.admin.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * @author hang.yu
 * @date 2022/7/13
 */
@Getter
public enum AdminErrorCodeEnum {

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

    // nft
    ERR_40001_NFT_TYPE_ALREADY_EXIST(40001, "NFT类别已存在", "NFT type code already exist"),
    ERR_40002_NFT_TYPE_PARENT_ITSELF(40002, "上级NFT类别不能为自身", "Parent nft type cannot be for itself"),
    ERR_40003_SUB_NFT_TYPE_EXIST(40003, "存在下级NFT类别", "Subordinate nft type exist"),
    ERR_40004_NFT_ALREADY_EXIST(40004, "NFT已存在", "NFT already exist"),
    ERR_40005_NFT_PROPERTIES_TYPE_ALREADY_EXIST(40005, "NFT属性类别已存在", "NFT properties type already exist"),
    ERR_40006_NFT_ON_SALE_CAN_NOT_BE_MODIFIED(40006, "NFT在售不能修改", "NFT is on sale and cannot be modified"),
    ERR_40007_NFT_LOCKED_CAN_NOT_BE_MODIFIED(40007, "NFT已锁定不能修改", "NFT is locked and cannot be modified"),
    ERR_40008_NFT_ON_SALE_CAN_NOT_LOCKED(40008, "NFT在售不能锁定", "NFT is on sale and cannot be locked"),
    ERR_40009_INSUFFICIENT_DURABILITY_VALUE_OF_NFT(40009, "NFT耐久不足", "Insufficient durability value of NFT"),
    ERR_40010_DUPLICATE_OR_MISSING_BASE_ATTRIBUTES_OF_NFT(40010, "NFT基础属性重复或缺失", "Duplicate or missing base attributes of NFT, type id:{%s}"),
    ERR_40011_NFT_PROPERTY_VALUE_IS_NOT_IN_THE_CORRECT_FORMAT(40011, "NFT属性值格式不正确", "NFT property value is not in the correct format, type id:{%s}"),


    // merchant
    ERR_50001_MERCHANT_ALREADY_EXIST(50001, "商家已存在", "Merchant already exist"),

    // dict type
    ERR_60001_DICT_TYPE_ALREADY_EXIST(60001,"字典类别已经存在","Dict type code already exist"),
    ERR_60002_DICT_TYPE_PARENT_ITSELF(60002,"上级字典类别不能为自身","Parent dict type cannot be for itself"),
    ERR_60003_SUB_DICT_TYPE_EXIST(60003,"存在下级字典类别","Subordinate dict type exist"),

    // meatamask
    ERR_70001_METAMASK_ADDRESS(70001, "非法地址格式", "Illegal address format"),
    ERR_70002_METAMASK_SIGNATURE(70002, "签名验证失败", "Signature verification failed"),
    ERR_70003_METAMASK_UNBIND_REPEATEDLY(70003, "重复解除绑定", "Unbind repeatedly"),
    ERR_17004_METAMASK_NONCE_INCORRECT(17004, "nonce不正确", "nonce is incorrect"),

    // game
    ERR_80001_GAME_COMMENTS_ALREADY_EXIST(80001, "您已经评价过该游戏", "You have already rated this game"),
    ERR_80002_GAME_ALREADY_EXIST(80002, "游戏已经存在", "Game already exist"),
    ERR_80003_GAME_TYPE_ALREADY_EXIST(80003, "游戏类别已存在", "NFT game code already exist"),
    ERR_80004_GAME_TYPE_PARENT_ITSELF(80004, "上级游戏类别不能为自身", "Parent game type cannot be for itself"),
    ERR_80005_SUB_GAME_TYPE_EXIST(80005, "存在下级游戏类别", "Subordinate game type exist"),


    // block chain

    ERR_90001_FAIL_TO_EXECUTE_ON_CHAIN(90001,"区块链操作失败", "chain operation failed"),

    //dict data
    ERR_100001_DICT_DATA_ALREADY_EXIST(100001,"字典已经存在","Dict data code already exist")
    ;

    @JsonValue
    @EnumValue
    private Integer code;
    private String desc;
    private String descEn;

    AdminErrorCodeEnum(Integer code, String desc, String descEn) {
        this.code = code;
        this.desc = desc;
        this.descEn = descEn;
    }
}