package com.seeds.account.mq.listener;

import cn.hutool.json.JSONUtil;
import com.seeds.account.dto.req.NftBuyCallbackReq;
import com.seeds.account.enums.CommonActionStatus;
import com.seeds.account.service.AccountTradeService;
import com.seeds.admin.dto.request.NftOwnerChangeReq;
import com.seeds.admin.feign.RemoteNftService;
import com.seeds.common.constant.mq.KafkaTopic;
import com.seeds.common.dto.GenericDto;
import com.seeds.uc.enums.UcErrorCodeEnum;
import com.seeds.uc.exceptions.GenericException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;


/**
 * 消费者
 *
 * @author hewei
 */
@Component
@Slf4j
public class AccountNftConsumer {

    @Autowired
    private RemoteNftService remoteNftService;

    @Autowired
    private AccountTradeService accountTradeService;

    /**
     * 消费NFT保存成功消息，完成NFT上链操作
     *
     * @param msg 消息
     */
    @KafkaListener(groupId = "account-nft-consumer-group", topics = {KafkaTopic.AC_NFT_OWNER_CHANGE})
    public void ownerChange(String msg) {
        log.info("收到消息：{}", msg);
        List<NftOwnerChangeReq> reqList = JSONUtil.toList(JSONUtil.parseArray(msg), NftOwnerChangeReq.class);
        try {
            GenericDto<Object> result = remoteNftService.ownerChange(reqList);
            if (!result.isSuccess()) {
                throw new GenericException(UcErrorCodeEnum.ERR_500_SYSTEM_BUSY);
            }
        } catch (Exception e) {
            log.error("NFT归属人变更失败");
            reqList.forEach(p -> {
                NftBuyCallbackReq callback = NftBuyCallbackReq.builder().build();
                callback.setActionStatusEnum(CommonActionStatus.FAILED);
                callback.setToUserId(p.getOwnerId());
                BeanUtils.copyProperties(p, callback);
                accountTradeService.buyNftCallback(callback);
            });
        }
    }

}
