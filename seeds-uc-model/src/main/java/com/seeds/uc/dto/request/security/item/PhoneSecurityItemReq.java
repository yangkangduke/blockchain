package com.seeds.uc.dto.request.security.item;

import lombok.Data;

/**
* @author yk
 * @date 2020/8/27
 */
@Data
public class PhoneSecurityItemReq {
    private String countryCode;
    private String phone;
    private String phoneToken;
    private String authToken;
}
