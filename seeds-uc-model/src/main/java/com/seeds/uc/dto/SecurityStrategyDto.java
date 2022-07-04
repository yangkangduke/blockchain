package com.seeds.uc.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

/**
 * @author allen
 * @email allen.hua.ai@gmail.com
 * @date 2020/8/26
 */
@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SecurityStrategyDto {
    @JsonProperty("verify_phone")
    private Boolean verifyPhone;
    @JsonProperty("verify_email")
    private Boolean verifyEmail;
    @JsonProperty("verify_ga")
    private Boolean verifyGA;
}
