package com.seeds.uc.controller;

import com.seeds.common.dto.GenericDto;
import com.seeds.uc.dto.response.RiskControlResp;
import com.seeds.uc.enums.CaptchaType;
import com.seeds.uc.enums.ClientRiskType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author allen
 * @email allen.hua.ai@gmail.com
 * @date 2020/7/28
 */
@Slf4j
@RestController
@RequestMapping("/uc-public/risk")
public class PublicRiskController {

    @PostMapping("control")
    public GenericDto<RiskControlResp> riskControl() {
        return GenericDto.success(RiskControlResp.builder()
                .risk(ClientRiskType.NO_RISK)
                .captchaType(CaptchaType.ALINGOOGLE)
                .build());
    }


}
