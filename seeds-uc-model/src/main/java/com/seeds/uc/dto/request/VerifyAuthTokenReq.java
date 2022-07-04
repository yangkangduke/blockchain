package com.seeds.uc.dto.request;

import com.seeds.uc.enums.AuthCodeUseTypeEnum;
import lombok.Builder;
import lombok.Data;

/**
 * @author allen
 * @email allen.hua.ai@gmail.com
 * @date 2020/9/3
 */
@Data
@Builder
public class VerifyAuthTokenReq {
    // 要验证的uid
    private Long uid;
    // 前端发过来的auth token
    private String authToken;
    // 类型，如提币
    private AuthCodeUseTypeEnum useType;
}
