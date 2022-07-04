package com.seeds.uc.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

/**
 * @author allen
 * @email allen.hua.ai@gmail.com
 * @date 2020/8/27
 */
@Data
@Builder
public class GoogleAuthResp {
    @JsonProperty("login_name")
    private String loginName;
    @JsonProperty("ga_key")
    private String gaKey;
    @JsonProperty("exchange_name")
    private String exchangeName;
}
