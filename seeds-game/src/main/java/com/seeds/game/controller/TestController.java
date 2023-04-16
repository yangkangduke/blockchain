package com.seeds.game.controller;

//import io.ipfs.api.IPFS;
//import io.ipfs.api.NamedStreamable;
//import io.ipfs.multihash.Multihash;
//

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;
import com.seeds.common.constant.mq.KafkaTopic;
import com.seeds.common.dto.GenericDto;
import com.seeds.game.dto.MetadataAttrDto;
import com.seeds.game.enums.NFTEnumConstant;
import com.seeds.game.mq.producer.KafkaProducer;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: hewei
 * @date 2023/4/15
 */
@Api(tags = "test")
@RestController
@RequestMapping("/public/web/test")
@Slf4j
public class TestController {
    @Autowired
    private KafkaProducer kafkaProducer;

    @GetMapping("test")
    public GenericDto<Object> test() {
        MetadataAttrDto metadataAttrDto = new MetadataAttrDto();
        metadataAttrDto.setConfigId(14070500L);
        metadataAttrDto.setAutoId(10011000030510L);
        int durability = 0;
        int rarityAttr = 0;
        try {
            JSONObject attr = JSONObject.parseObject("{\"durability\": 19, \"rarityAttr\": 45}");
            durability = (int) attr.get("durability");
            rarityAttr = (int) attr.get("rarityAttr");
        } catch (Exception e) {
            e.printStackTrace();
        }
        metadataAttrDto.setTokenId(794);
        metadataAttrDto.setDurability(durability);
        metadataAttrDto.setQuality(5);
        metadataAttrDto.setRareAttribute(rarityAttr);
        metadataAttrDto.setImage("https://gateway.pinata.cloud/ipfs/QmcVCBR9giX7H3fbEnRtfiCUYwJ4yjQVhHGuqMnUPQqP1c");
        metadataAttrDto.setName(NFTEnumConstant.TokenNamePreEnum.SEQN.getName() + 794);
        kafkaProducer.sendAsync(KafkaTopic.NFT_MINT_SUCCESS, JSONUtil.toJsonStr(metadataAttrDto));
        log.info("发送【metadata】数据：{}", JSONUtil.toJsonStr(metadataAttrDto));
        return GenericDto.success(null);
    }
}
