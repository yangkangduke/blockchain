package com.seeds.account.monitor;

import cn.hutool.core.date.DateUtil;
import com.seeds.account.dto.ChainDepositWithdrawMonitorDto;
import com.seeds.account.enums.AccountAction;
import com.seeds.account.service.IChainDepositWithdrawHisService;
import com.seeds.account.util.MetricsGaugeUtils;
import com.seeds.common.enums.Chain;
import io.micrometer.core.instrument.Tags;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 每日冲提币总数统计
 *
 * @author hewei
 */
@Service
@Slf4j
public class DepositWithdrawMonitorService {
    @Autowired
    private IChainDepositWithdrawHisService chainDepositWithdrawHisService;

    @Scheduled(fixedDelay = 10 * 1000, initialDelay = 10 * 1000)
    public void monitor() throws Exception {
        log.info("DepositWithdrawMonitorService start ...");

        long startTime = DateUtil.beginOfDay(new Date()).getTime();
        long endTime = DateUtil.endOfDay(new Date()).getTime();
        List<ChainDepositWithdrawMonitorDto> result = chainDepositWithdrawHisService.getDailyDepositWithdraw(startTime, endTime);
        if (null != result) {
            result.forEach(p -> {
                MetricsGaugeUtils.gauge("user-deposit-withdraw",
                        Tags.of("chain", p.getChain() == Chain.ETH.getCode() ? Chain.ETH.getName() : Chain.TRON.getName(), "action", p.getAction() == AccountAction.WITHDRAW.getCode() ? AccountAction.WITHDRAW.getNotificationType() : AccountAction.DEPOSIT.getNotificationType()), p.getAmount().doubleValue());
            });
        }
        log.info("DepositWithdrawMonitorService end ...");
    }

}
