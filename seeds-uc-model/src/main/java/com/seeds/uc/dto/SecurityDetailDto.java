package com.seeds.uc.dto;

import lombok.Builder;
import lombok.Data;

/**
* @author yk
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
    private String phantom;
    private Boolean phantomState;
}
