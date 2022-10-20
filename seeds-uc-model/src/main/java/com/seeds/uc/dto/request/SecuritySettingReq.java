package com.seeds.uc.dto.request;

import com.seeds.uc.enums.AuthCodeUseTypeEnum;
import lombok.Data;

/**
 * @author yk
 * @date 2020/8/26
 */
@Data
public class SecuritySettingReq {
//    private ClientAuthTypeEnum item;
    private String emailCode;
//    private String gaCode;
    private AuthCodeUseTypeEnum useType;
//    private String authToken;
}
