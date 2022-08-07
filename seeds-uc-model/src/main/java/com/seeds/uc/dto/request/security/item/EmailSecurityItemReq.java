package com.seeds.uc.dto.request.security.item;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author allen
 * @email allen.hua.ai@gmail.com
 * @date 2020/8/27
 */
@Data
public class EmailSecurityItemReq {
//    private String email;
    @JsonProperty("email_token")
    private String emailToken;
    private String password;
//    @JsonProperty("auth_token")
//    private String authToken;
}
