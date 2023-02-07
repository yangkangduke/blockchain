package com.seeds.uc.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

/**
* @author yk
 * @date 2020/8/26
 */
@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SecurityStrategyDto {
    private Boolean verifyMetamask;
    private Boolean verifyPhantom;
    private Boolean verifyEmail;
    private Boolean verifyGA;
}
