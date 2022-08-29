package com.seeds.uc.dto.request;

import com.seeds.uc.enums.AuthCodeUseTypeEnum;
import lombok.Data;

/**
 * @author allen
 * @email allen.hua.ai@gmail.com
 * @date 2020/8/27
 */
@Data
public class GoogleAuthVerifyReq {
    private String gaCode;
    private AuthCodeUseTypeEnum useType;
}
