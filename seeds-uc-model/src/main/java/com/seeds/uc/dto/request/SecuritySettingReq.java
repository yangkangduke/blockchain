package com.seeds.uc.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.seeds.uc.enums.AuthCodeUseTypeEnum;
import com.seeds.uc.enums.ClientAuthTypeEnum;
import lombok.Data;

/**
 * @author allen
 * @email allen.hua.ai@gmail.com
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
