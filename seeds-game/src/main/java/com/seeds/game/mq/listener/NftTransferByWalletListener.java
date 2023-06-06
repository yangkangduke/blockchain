package com.seeds.game.mq.listener;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.seeds.common.constant.mq.KafkaTopic;
import com.seeds.common.dto.GenericDto;
import com.seeds.game.dto.request.external.NftTransferByWalletDto;
import com.seeds.game.entity.NftPublicBackpackEntity;
import com.seeds.game.enums.NFTEnumConstant;
import com.seeds.game.service.INftPublicBackpackService;
import com.seeds.uc.dto.response.UcUserResp;
import com.seeds.uc.feign.UserCenterFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * 处理用户通过钱包转移NFT的消息
 *
 * @author: he.wei
 * @date 2023/5/30
 */

@Slf4j
@Component
public class NftTransferByWalletListener {

    @Resource
    private INftPublicBackpackService nftPublicBackpackService;
    @Resource
    private UserCenterFeignClient userCenterFeignClient;

    @KafkaListener(groupId = "nft-transfer-by-wallet", topics = {KafkaTopic.NFT_TRANSFER_SUCCESS_BY_PHANTOM})
    public void nftTransferSuccess(String msg) {
        log.info("收到nft transfer by wallet消息，msg:{}", msg);
        NftTransferByWalletDto nftTransferByWalletDto = JSONUtil.toBean(msg, NftTransferByWalletDto.class);
        if (Objects.nonNull(nftTransferByWalletDto)) {
            NftPublicBackpackEntity one = nftPublicBackpackService.getOne(new LambdaQueryWrapper<NftPublicBackpackEntity>()
                    .eq(NftPublicBackpackEntity::getTokenAddress, nftTransferByWalletDto.getMintAddress()));
            if (Objects.nonNull(one) && nftTransferByWalletDto.getFromAddress().equals(one.getOwner())) {
                UcUserResp userResp = null;
                try {
                    GenericDto<UcUserResp> result = userCenterFeignClient.getByPublicAddress(nftTransferByWalletDto.getToAddress());
                    userResp = result.getData();
                } catch (Exception e) {
                    log.error("内部请求uc获取用户信息失败.{}", e.getMessage());
                }
                one.setUserId(Objects.nonNull(userResp) ? userResp.getId() : 0L);
                one.setOwner(nftTransferByWalletDto.getToAddress());
                one.setState(NFTEnumConstant.NFTStateEnum.UNDEPOSITED.getCode());
                nftPublicBackpackService.updateById(one);
            }
        }
    }
}