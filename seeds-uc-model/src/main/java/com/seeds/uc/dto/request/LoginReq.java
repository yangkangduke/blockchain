package com.seeds.uc.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.seeds.uc.enums.ClientAuthTypeEnum;
import lombok.Data;

/**
 * @author allen
 * @email allen.hua.ai@gmail.com
 * @date 2020/7/28
 */
@Data
public class LoginReq {
    @JsonProperty("login_name")
    private String loginName;
    private String password;
    private ClientAuthTypeEnum loginType;
}
