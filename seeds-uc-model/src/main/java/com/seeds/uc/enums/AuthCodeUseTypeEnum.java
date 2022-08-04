package com.seeds.uc.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

import java.util.EnumSet;
import java.util.Objects;
import java.util.Set;

/**
 * @author yk
 * @email 819628513@qq.com
 * @date 2022/07/13
 */
@Getter
public enum AuthCodeUseTypeEnum {
    // 登录
    @JsonProperty("LOGIN")
    LOGIN("LOGIN", "login", "登录", "login"),
    @JsonProperty("LITE_LOGIN")
    LITE_LOGIN("LITE_LOGIN", "lite login", "轻登陆", "lite.login"),
    @JsonProperty("REGISTER")
    REGISTER("REGISTER", "register", "注册", "reg"),
    @JsonProperty("RESET_PASSWORD")
    RESET_PASSWORD("RESET_PASSWORD", "reset password", "重置密码", "reset.pwd"),
    @JsonProperty("CHANGE_PASSWORD")
    CHANGE_PASSWORD("CHANGE_PASSWORD", "change password", "修改密码", "change.pwd"),
    @JsonProperty("CHANGE_EMAIL")
    CHANGE_EMAIL("CHANGE_EMAIL", "change email", "修改邮箱", "change.email"),
    @JsonProperty("BIND_PHONE")
    BIND_PHONE("BIND_PHONE", "bind phone", "绑定手机", "bind.phone"),
    @JsonProperty("REBIND_PHONE")
    REBIND_PHONE("REBIND_PHONE", "rebind phone", "重新绑定手机", "rebind.phone"),
    @JsonProperty("BIND_EMAIL")
    BIND_EMAIL("BIND_EMAIL", "bind email", "绑定邮箱", "bind.email"),
    @JsonProperty("RESET_PHONE")
    RESET_PHONE("RESET_PHONE", "reset phone", "重置手机", "rst.phone"),
    @JsonProperty("VERIFY_SETTING_POLICY")
    VERIFY_SETTING_POLICY("VERIFY_SETTING_POLICY", "verify setting policy", "验证安全策略", "vsp"),
    @JsonProperty("VERIFY_SETTING_POLICY_BIND_EMAIL")
    VERIFY_SETTING_POLICY_BIND_EMAIL("VERIFY_SETTING_POLICY_BIND_EMAIL",
            "verify setting policy: bind email",
            "验证安全策略-绑定邮箱",
            "vsp.be"),
    @JsonProperty("VERIFY_SETTING_POLICY_BIND_PHONE")
    VERIFY_SETTING_POLICY_BIND_PHONE("VERIFY_SETTING_POLICY_BIND_PHONE",
            "verify setting policy: bind phone",
            "验证安全策略-绑定手机",
            "vsp.bp"),
    @JsonProperty("VERIFY_SETTING_POLICY_REBIND_PHONE")
    VERIFY_SETTING_POLICY_REBIND_PHONE("VERIFY_SETTING_POLICY_REBIND_PHONE",
            "verify setting policy: rebind phone",
            "验证安全策略-更换手机",
            "vsp.rp"),
    @JsonProperty("VERIFY_SETTING_POLICY_BIND_GA")
    VERIFY_SETTING_POLICY_BIND_GA("VERIFY_SETTING_POLICY_BIND_GA",
            "verify setting policy: bind ga",
            "验证安全策略-绑定GA",
            "vsp.bg"),
    @JsonProperty("VERIFY_SETTING_POLICY_REBIND_GA")
    VERIFY_SETTING_POLICY_REBIND_GA("VERIFY_SETTING_POLICY_REBIND_GA",
            "verify setting policy: rebind ga",
            "验证安全策略-更换GA",
            "vsp.rg"),
    @JsonProperty("VERIFY_SETTING_POLICY_ENABLE_PHONE_POLICY")
    VERIFY_SETTING_POLICY_ENABLE_PHONE_POLICY("VERIFY_SETTING_POLICY_ENABLE_PHONE_POLICY",
            "verify setting policy: enable phone policy",
            "验证安全策略-开启手机安全策略",
            "vsp.epp"),
    @JsonProperty("VERIFY_SETTING_POLICY_DISABLE_PHONE_POLICY")
    VERIFY_SETTING_POLICY_DISABLE_PHONE_POLICY("VERIFY_SETTING_POLICY_DISABLE_PHONE_POLICY",
            "verify setting policy: disable phone policy",
            "验证安全策略-关闭手机安全策略",
            "vsp.dpp"),
    @JsonProperty("VERIFY_SETTING_POLICY_ENABLE_EMAIL_POLICY")
    VERIFY_SETTING_POLICY_ENABLE_EMAIL_POLICY("VERIFY_SETTING_POLICY_ENABLE_EMAIL_POLICY",
            "verify setting policy: enable email policy",
            "验证安全策略-开启邮箱安全策略",
            "vsp.eep"),
    @JsonProperty("VERIFY_SETTING_POLICY_DISABLE_EMAIL_POLICY")
    VERIFY_SETTING_POLICY_DISABLE_EMAIL_POLICY("VERIFY_SETTING_POLICY_DISABLE_EMAIL_POLICY",
            "verify setting policy: disable email policy",
            "验证安全策略-关闭邮箱安全策略",
            "vsp.dep"),
    @JsonProperty("VERIFY_SETTING_POLICY_ENABLE_GA_POLICY")
    VERIFY_SETTING_POLICY_ENABLE_GA_POLICY("VERIFY_SETTING_POLICY_ENABLE_GA_POLICY",
            "verify setting policy: enable ga policy",
            "验证安全策略-开启GA安全策略",
            "vsp.egp"),
    @JsonProperty("VERIFY_SETTING_POLICY_DISABLE_GA_POLICY")
    VERIFY_SETTING_POLICY_DISABLE_GA_POLICY("VERIFY_SETTING_POLICY_DISABLE_GA_POLICY",
            "verify setting policy: disable ga policy",
            "验证安全策略-关闭GA安全策略",
            "vsp.dgp"),

    @JsonProperty("VERIFY_SETTING_POLICY_WITHDRAW")
    VERIFY_SETTING_POLICY_WITHDRAW("VERIFY_SETTING_POLICY_WITHDRAW",
            "verify setting policy: withdraw",
            "提币（法币、数字货币）",
            "vsp.w"),
    @JsonProperty("VERIFY_SETTING_POLICY_WITHDRAW_ADD_ADDRESS")
    VERIFY_SETTING_POLICY_WITHDRAW_ADD_ADDRESS("VERIFY_SETTING_POLICY_WITHDRAW_ADD_ADDRESS",
            "verify setting policy: withdraw add address",
            "添加提币地址",
            "vsp.waa"),


    @JsonProperty("PAY_PASSWORD_RESET")
    PAY_PASSWORD_RESET("PAY_PASSWORD_RESET",
            "pay password reset",
            "重置资金密码",
            "pay.pwd.rst"),
    @JsonProperty("CHANGE_LOGIN_PASSWORD")
    CHANGE_LOGIN_PASSWORD("CHANGE_LOGIN_PASSWORD",
            "change login password",
            "变更登录密码",
            "chg.login.pwd"),
    @JsonProperty("RESET_ASSET_PASSWORD")
    RESET_ASSET_PASSWORD("RESET_ASSET_PASSWORD",
            "reset asset password",
            "重置资产密码",
            "rst.asset.pwd"),
    @JsonProperty("RESET_LOGIN_PASSWORD")
    RESET_LOGIN_PASSWORD("RESET_LOGIN_PASSWORD",
            "reset login password",
            "重置登录密码",
            "rst.login.pwd"),
    ;
    /**
     * code 登陆以及联系方式读取类型总结
     */

    // 不需要登陆，需要发的手机号/邮箱号从请求里读
    public static final Set<AuthCodeUseTypeEnum> CODE_NO_NEED_LOGIN_READ_REQUEST =
            EnumSet.of(REGISTER);
    // 不需要登陆，需要发的手机号/邮箱号从DB里读
    public static final Set<AuthCodeUseTypeEnum> CODE_NO_NEED_LOGIN_READ_DB =
            EnumSet.of(LOGIN);
    // 发sms code的时候，需要登陆，且手机号从DB里读的类型
    public static final Set<AuthCodeUseTypeEnum> SMS_NEED_LOGIN_READ_DB_SET =
            EnumSet.of(VERIFY_SETTING_POLICY_BIND_EMAIL,
                    VERIFY_SETTING_POLICY_BIND_GA,
                    VERIFY_SETTING_POLICY_REBIND_GA,
                    VERIFY_SETTING_POLICY_ENABLE_PHONE_POLICY,
                    VERIFY_SETTING_POLICY_DISABLE_PHONE_POLICY,
                    VERIFY_SETTING_POLICY_DISABLE_EMAIL_POLICY,
                    VERIFY_SETTING_POLICY_DISABLE_GA_POLICY,
                    VERIFY_SETTING_POLICY_REBIND_PHONE,
                    VERIFY_SETTING_POLICY_WITHDRAW,
                    VERIFY_SETTING_POLICY_WITHDRAW_ADD_ADDRESS
            );
    // 发sms code的时候，需要登陆，且手机号从请求里读的类型
    public static final Set<AuthCodeUseTypeEnum> SMS_NEED_LOGIN_READ_REQUEST_SET =
            EnumSet.of(BIND_PHONE, REBIND_PHONE);
    // 发email code的时候，需要登陆，且邮箱从request里读的类型
    public static final Set<AuthCodeUseTypeEnum> EMAIL_NEED_LOGIN_READ_REQUEST_SET =
            EnumSet.of(BIND_EMAIL);
    // 发email code的时候，需要登陆，且邮箱从DB里读的类型
    public static final Set<AuthCodeUseTypeEnum> EMAIL_NEED_LOGIN_READ_DB_SET =
            EnumSet.of(VERIFY_SETTING_POLICY_BIND_PHONE,
                    VERIFY_SETTING_POLICY_REBIND_PHONE,
                    VERIFY_SETTING_POLICY_BIND_GA,
                    VERIFY_SETTING_POLICY_REBIND_GA,
                    VERIFY_SETTING_POLICY_DISABLE_PHONE_POLICY,
                    VERIFY_SETTING_POLICY_ENABLE_EMAIL_POLICY,
                    VERIFY_SETTING_POLICY_DISABLE_EMAIL_POLICY,
                    VERIFY_SETTING_POLICY_DISABLE_GA_POLICY,
                    VERIFY_SETTING_POLICY_REBIND_PHONE,
                    VERIFY_SETTING_POLICY_WITHDRAW,
                    VERIFY_SETTING_POLICY_WITHDRAW_ADD_ADDRESS
            );
    // security setting 时Phone相关的use type
    public static final Set<AuthCodeUseTypeEnum> PHONE_SECURITY_SETTING_SET =
            EnumSet.of(VERIFY_SETTING_POLICY_ENABLE_PHONE_POLICY, VERIFY_SETTING_POLICY_DISABLE_PHONE_POLICY);
    // security item change Phone相关的use type
    public static final Set<AuthCodeUseTypeEnum> PHONE_SECURITY_ITEM_SET =
            EnumSet.of(VERIFY_SETTING_POLICY_BIND_PHONE);
    // Email相关的use type
    public static final Set<AuthCodeUseTypeEnum> EMAIL_SECURITY_SETTING_SET =
            EnumSet.of(VERIFY_SETTING_POLICY_ENABLE_EMAIL_POLICY, VERIFY_SETTING_POLICY_DISABLE_EMAIL_POLICY);
    // security item change Email相关的use type
    public static final Set<AuthCodeUseTypeEnum> EMAIL_SECURITY_ITEM_SET =
            EnumSet.of(VERIFY_SETTING_POLICY_BIND_EMAIL);
    // GA相关的use type
    public static final Set<AuthCodeUseTypeEnum> GA_SECURITY_SETTING_SET =
            EnumSet.of(VERIFY_SETTING_POLICY_ENABLE_GA_POLICY, VERIFY_SETTING_POLICY_DISABLE_GA_POLICY);
    // security item change GA相关的use type
    public static final Set<AuthCodeUseTypeEnum> GA_SECURITY_ITEM_SET =
            EnumSet.of(VERIFY_SETTING_POLICY_BIND_GA);
    // 是security item集中的use type， security verify的时候要忽略这项
    public static final Set<AuthCodeUseTypeEnum> STRATEGY_VERIFY_IGNORE =
            EnumSet.of(VERIFY_SETTING_POLICY_BIND_PHONE, VERIFY_SETTING_POLICY_BIND_EMAIL, VERIFY_SETTING_POLICY_BIND_GA);
    @JsonValue
    private final String code;
    private final String desc;
    private final String descCn;
    private final String brief;

    AuthCodeUseTypeEnum(String code, String desc, String descCn, String brief) {
        this.code = code;
        this.desc = desc;
        this.descCn = descCn;
        this.brief = brief;
    }

    public static AuthCodeUseTypeEnum fromCode(String code) {
        for (AuthCodeUseTypeEnum o : values()) {
            if (Objects.equals(o.code, code)) {
                return o;
            }
        }
        return null;
    }

}