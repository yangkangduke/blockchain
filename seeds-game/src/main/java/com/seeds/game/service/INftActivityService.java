package com.seeds.game.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.seeds.game.dto.request.NftActivityPageReq;
import com.seeds.game.dto.response.NftActivityResp;
import com.seeds.game.entity.NftActivity;


/**
 * <p>
 * nft活动记录
 * </p>
 *
 * @author hang.yu
 * @since 2023-03-28
 */
public interface INftActivityService extends IService<NftActivity> {

    /**
     * 分页获取NFT活动信息
     * @param req 分页查询条件
     * @return NFT活动信息
     */
    IPage<NftActivityResp> queryPage(NftActivityPageReq req);

    /**
     * 获取NFT最后活动时间
     * @param mintAddress NFT地址
     * @return NFT最后活动时间
     */
    Long queryLastUpdateTime(String mintAddress);

}
