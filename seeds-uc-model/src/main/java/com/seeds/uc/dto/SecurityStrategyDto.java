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
    private Boolean verifyMetamask;
    private Boolean verifyEmail;
    private Boolean verifyGA;
}
