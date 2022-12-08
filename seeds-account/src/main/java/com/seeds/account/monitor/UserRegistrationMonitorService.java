package com.seeds.account.monitor;

import com.seeds.account.util.MetricsGaugeUtils;
import com.seeds.common.dto.GenericDto;
import com.seeds.uc.dto.response.UserRegistrationResp;
import com.seeds.uc.feign.UserCenterFeignClient;
import io.micrometer.core.instrument.Tags;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * 用户注册情况监控
 *
 * @author hewei
 */
@Service
@Slf4j
public class UserRegistrationMonitorService {
    @Autowired
    private UserCenterFeignClient userCenterFeignClient;

    @Scheduled(fixedDelay = 60 * 1000, initialDelay = 10 * 1000)
    public void monitor() throws Exception {
        log.info("UserRegistrationMonitorService start ...");

        GenericDto<UserRegistrationResp> respGenericDto = userCenterFeignClient.getUserRegistration();
        if (null != respGenericDto.getData()) {
            UserRegistrationResp data = respGenericDto.getData();
            MetricsGaugeUtils.gauge("user-registration", Tags.of("total"), data.getTotalRegisteredUsers());
            MetricsGaugeUtils.gauge("user-registration", Tags.of("today"), data.getTodayRegisteredUsers());

        }
        log.info("UserRegistrationMonitorService end ...");
    }
}
