package com.seeds.uc.mq.listener;

import cn.hutool.json.JSONUtil;
import com.seeds.admin.dto.request.NftOwnerChangeReq;
import com.seeds.admin.feign.RemoteNftService;
import com.seeds.common.constant.mq.KafkaTopic;
import com.seeds.common.dto.GenericDto;
import com.seeds.uc.dto.request.NFTBuyCallbackReq;
import com.seeds.uc.enums.AccountActionStatusEnum;
import com.seeds.uc.enums.UcErrorCodeEnum;
import com.seeds.uc.exceptions.GenericException;
import com.seeds.uc.service.UcInterNFTService;
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
public class UcNFTConsumer {

    @Autowired
    private RemoteNftService remoteNftService;

    @Autowired
    private UcInterNFTService ucInterNftService;

    /**
     * 消费NFT保存成功消息，完成NFT上链操作
     *
     * @param msg 消息
     */
    @KafkaListener(groupId = "uc-nft-consumer-group", topics = {KafkaTopic.UC_NFT_OWNER_CHANGE})
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
                NFTBuyCallbackReq callback = NFTBuyCallbackReq.builder().build();
                callback.setActionStatusEnum(AccountActionStatusEnum.FAIL);
                callback.setToUserId(p.getOwnerId());
                BeanUtils.copyProperties(p, callback);
                ucInterNftService.buyNFTCallback(callback);
            });
        }
    }

}
