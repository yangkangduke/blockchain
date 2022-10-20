package com.seeds.uc.dto.response;

import lombok.Builder;
import lombok.Data;

/**
* @author yk
 * @date 2020/8/26
 */
@Data
@Builder
public class TokenResp {
    private String token;
}
