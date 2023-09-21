package com.seeds.uc.dto.response;

import lombok.Builder;
import lombok.Data;

/**
* @author yk
 * @date 2020/8/27
 */
@Data
@Builder
public class GoogleAuthResp {
    private String loginName;
    private String gaKey;
    private String exchangeName;
}