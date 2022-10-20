package com.seeds.uc.dto.request.security.item;

import lombok.Data;

/**
* @author yk
 * @date 2020/8/27
 */
@Data
public class GaSecurityItemReq {
    private String gaToken;
    private String authToken;
}
