package com.seeds.uc.model.user.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.seeds.uc.model.user.enums.ClientAuthTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;

/**
 * @author yk
 * @email 819628513@qq.com
 * @date 2022/07/13
 */
@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class LoginResp {

    @ApiModelProperty(value = "the real token for login proof")
    private String ucToken;
    @ApiModelProperty(value = "邮箱")
    @Email
    private String email;
    private ClientAuthTypeEnum type;

}