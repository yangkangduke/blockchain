package com.seeds.uc.job;

import com.seeds.account.model.NftOffer;
import com.seeds.uc.service.impl.CacheService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;


/**
 * <p>
 * NFT任务 前端控制器
 * </p>
 *
 * @author hang.yu
 * @since 2022-09-06
 */
@Component
@Slf4j
@RestController
@RequestMapping("/public/nft-task")
@Api(tags = "NFT任务")
public class UcNftTask {

    @Autowired
    private CacheService cacheService;

    //@Autowired
    //private IUcNftOfferService ucNftOfferService;


    @Scheduled(cron = "0 0 3 * * ?")
    @PostMapping("/expired-offer")
    @ApiOperation(value = "过期offer处理", notes = "过期offer处理")
    public void expiredOffer() {
        /*String key = LocalDate.now().toString();
        if (StringUtils.isNotBlank(cacheService.getOneDayMarking(key))) {
            log.info("有正在执行中的过期offer处理任务， key={}", key);
            return;
        }
        // 查询过期的offer
        List<NftOffer> offers = ucNftOfferService.queryExpiredOffers();
        if (CollectionUtils.isEmpty(offers)) {
            log.info("没有过期的offer需要处理， time={}", new Date());
            return;
        }
        cacheService.putOneDayMarking(key);
        offers.forEach(p -> {
            try {
                // 执行任务
                processTask(p);
            } catch (Exception e) {
                log.info("处理过期的offer任务失败， offer id ={}", p.getId());
            }
        });
        cacheService.removeOneDayMarking(key);*/
    }

    @Transactional(rollbackFor = Exception.class)
    public void processTask(NftOffer offer){
        Long userId = offer.getUserId();
        // 解冻金额
        BigDecimal price = offer.getPrice();
//        UcUserAccountInfoResp info = ucUserAccountService.getInfoByUserId(userId);
//        ucUserAccountService.update(UcUserAccount.builder()
//                .freeze(info.getFreeze().subtract(price))
//                .balance(info.getBalance().add(price))
//                .build(), new LambdaQueryWrapper<UcUserAccount>()
//                .eq(UcUserAccount::getUserId, userId)
//                .eq(UcUserAccount::getCurrency, CurrencyEnum.USDT));
//        // 修改记录信息
//        ucUserAccountActionHistoryService.update(UcUserAccountActionHistory.builder()
//                .status(AccountActionStatusEnum.FAIL)
//                .build(), new LambdaQueryWrapper<UcUserAccountActionHistory>()
//                .eq(UcUserAccountActionHistory::getId, offer.getActionHistoryId()));
        // 修改offer
//        offer.setStatus(NFTOfferStatusEnum.EXPIRED);
//        ucNftOfferService.updateById(offer);
    }

}
