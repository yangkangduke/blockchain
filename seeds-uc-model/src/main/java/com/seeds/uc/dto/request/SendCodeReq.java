package com.seeds.uc.dto.request;

import com.seeds.uc.enums.AuthCodeUseTypeEnum;
import lombok.Builder;
import lombok.Data;

/**
 * @author allen
 * @email allen.hua.ai@gmail.com
 * @date 2020/8/2
 */
@Data
@Builder
public class SendCodeReq {
    private String email;
    private String phone;
    private AuthCodeUseTypeEnum useType;
    private String countryCode;
}
