package com.seeds.uc.dto.request;

import com.seeds.uc.enums.AuthCodeUseTypeEnum;
import lombok.Data;

/**
* @author yk
 * @date 2020/8/27
 */
@Data
public class GoogleAuthVerifyReq {
    private String gaCode;
    private AuthCodeUseTypeEnum useType;
}
