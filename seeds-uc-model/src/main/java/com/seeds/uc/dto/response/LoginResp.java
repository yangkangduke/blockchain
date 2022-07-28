package com.seeds.uc.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.seeds.uc.enums.ClientAuthTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

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
    private String token;
    private String account;
    private ClientAuthTypeEnum type;

}