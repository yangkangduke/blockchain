package com.seeds.uc.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.seeds.uc.enums.CaptchaType;
import com.seeds.uc.enums.ClientRiskType;
import lombok.Builder;
import lombok.Data;

/**
 * @author allen
 * @email allen.hua.ai@gmail.com
 * @date 2020/8/2
 */
@Data
@Builder
public class RiskControlResp {
    private ClientRiskType risk;
    @JsonProperty("captcha_type")
    private CaptchaType captchaType;
}
