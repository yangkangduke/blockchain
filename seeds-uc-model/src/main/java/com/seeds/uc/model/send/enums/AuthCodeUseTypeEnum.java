package com.seeds.uc.model.send.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

import java.util.Objects;

/**
 * @author allen
 * @email allen.hua.ai@gmail.com
 * @date 2020/8/1
 */
@Getter
public enum AuthCodeUseTypeEnum {
    @JsonProperty("REGISTER")
    REGISTER("REGISTER", "register", "注册", "reg"),
    @JsonProperty("BIND_EMAIL")
    BIND_EMAIL("BIND_EMAIL", "bind email", "绑定邮箱", "bind.email"),
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