package com.seeds.uc.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.seeds.uc.enums.AuthCodeUseTypeEnum;
import lombok.Data;

/**
 * @author allen
 * @email allen.hua.ai@gmail.com
 * @date 2020/8/27
 */
@Data
public class GoogleAuthVerifyReq {
    @JsonProperty("ga_code")
    private String gaCode;
    @JsonProperty("use_type")
    private AuthCodeUseTypeEnum useType;
}
