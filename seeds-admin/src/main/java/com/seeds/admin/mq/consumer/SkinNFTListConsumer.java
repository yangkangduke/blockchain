package com.seeds.admin.mq.consumer;

/**
 * @author: he.wei
 * @date 2023/5/26
 */

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.seeds.admin.entity.SysNftPicEntity;
import com.seeds.admin.enums.SkinNftEnums;
import com.seeds.admin.service.SysNftPicService;
import com.seeds.common.constant.mq.KafkaTopic;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class SkinNFTListConsumer {

    @Autowired
    private SysNftPicService nftPicService;

    @KafkaListener(groupId = "skin-nft-list-success", topics = {KafkaTopic.SKIN_NFT_LIST_ASSET_SUCCESS})
    public void skinListSuccess(String msg) {
        log.info("收到skin-nft-list-success消息：{}", msg);
        JSONArray objects = JSONUtil.parseArray(msg);
        List<Long> ids = objects.toList(Long.class);
        List<SysNftPicEntity> list = nftPicService.listByIds(ids);
        list.forEach(p -> p.setListState(SkinNftEnums.SkinNftListStateEnum.LISTED.getCode()));
        nftPicService.updateBatchById(list);
    }

    @KafkaListener(groupId = "skin-nft-cancel-list-success", topics = {KafkaTopic.SKIN_NFT_CANCEL_ASSET_SUCCESS})
    public void skinCancelList(String msg) {
        log.info("收到skin-nft-cancel-list-success消息：{}", msg);
        JSONArray objects = JSONUtil.parseArray(msg);
        List<Long> ids = objects.toList(Long.class);
        List<SysNftPicEntity> list = nftPicService.listByIds(ids);
        list.forEach(p -> p.setListState(SkinNftEnums.SkinNftListStateEnum.NO_LIST.getCode()));
        nftPicService.updateBatchById(list);
    }
}