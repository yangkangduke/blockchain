package com.seeds.uc.dto;

import lombok.Builder;
import lombok.Data;

/**
 * @author allen
 * @email allen.hua.ai@gmail.com
 * @date 2020/8/26
 */
@Data
@Builder
public class SecurityDetailDto {
    private String metamask;
    private Boolean metamaskState;
    private String email;
    private Boolean emailState;
    private Boolean gaState;
}
