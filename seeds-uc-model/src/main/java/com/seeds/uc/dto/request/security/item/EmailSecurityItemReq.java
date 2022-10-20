package com.seeds.uc.dto.request.security.item;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
* @author yk
 * @date 2020/8/27
 */
@Data
public class EmailSecurityItemReq {
    private String email;
    private String emailToken;
    @NotBlank
    private String password;
    private String authToken;
}
