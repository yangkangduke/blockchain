package com.seeds.uc.model.user.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.seeds.uc.model.user.enums.ClientAuthTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

/**
 * @author allen
 * @email allen.hua.ai@gmail.com
 * @date 2020/7/31
 */
@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class LoginResp {

    @ApiModelProperty(value = "the real token for login proof")
    private String ucToken;
    @ApiModelProperty(value = "邮箱")
    private String email;
    private ClientAuthTypeEnum type;

}