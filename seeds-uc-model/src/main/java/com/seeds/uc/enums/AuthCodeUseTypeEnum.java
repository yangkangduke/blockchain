package com.seeds.uc.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

import java.util.Objects;

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
    REGISTER_EMAIL("REGISTER_EMAIL", "email register", "注册邮箱账号", "reg.email"),
    @JsonProperty("VERIFY_SETTING_POLICY")
    VERIFY_SETTING_POLICY("VERIFY_SETTING_POLICY", "verify setting policy", "验证安全策略", "vsp"),
    @JsonProperty("VERIFY_SETTING_POLICY_BIND_EMAIL")
    VERIFY_SETTING_POLICY_BIND_EMAIL("VERIFY_SETTING_POLICY_BIND_EMAIL",
            "verify setting policy: bind email",
            "验证安全策略-绑定邮箱",
            "vsp.be"),
    @JsonProperty("VERIFY_SETTING_POLICY_BIND_GA")
    VERIFY_SETTING_POLICY_BIND_GA("VERIFY_SETTING_POLICY_BIND_GA",
            "verify setting policy: bind ga",
            "验证安全策略-绑定GA",
            "vsp.bg"),
    @JsonProperty("VERIFY_SETTING_POLICY_REBIND_GA")
    VERIFY_SETTING_POLICY_REBIND_GA("VERIFY_SETTING_POLICY_REBIND_GA",
            "verify setting policy: rebind ga",
            "验证安全策略-更换GA",
            "vsp.rg");

    /**
     * code 登陆以及联系方式读取类型总结
     */
    @JsonValue
    @EnumValue
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