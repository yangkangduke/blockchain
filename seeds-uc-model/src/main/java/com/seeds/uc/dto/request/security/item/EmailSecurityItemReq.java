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
    private String emailToken;
    private String password;
//    private String authToken;
}
