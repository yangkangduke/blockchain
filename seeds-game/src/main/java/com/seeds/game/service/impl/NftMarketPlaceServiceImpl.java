package com.seeds.game.service.impl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.seeds.common.web.context.UserContext;
import com.seeds.game.dto.request.NftBuySuccessReq;
import com.seeds.game.dto.request.NftMarketPlaceDetailViewReq;
import com.seeds.game.dto.request.NftMarketPlaceEquipPageReq;
import com.seeds.game.dto.request.NftMarketPlaceSkinPageReq;
import com.seeds.common.dto.PageReq;
import com.seeds.game.dto.request.*;
import com.seeds.game.dto.response.NftActivityResp;
import com.seeds.game.dto.response.NftMarketPlaceDetailResp;
import com.seeds.game.dto.response.NftMarketPlaceDetailViewResp;
import com.seeds.game.dto.response.NftMarketPlaceEqiupmentResp;
import com.seeds.game.dto.response.NftMarketPlaceSkinResp;
import com.seeds.game.entity.NftAttributeEntity;
import com.seeds.game.entity.NftPublicBackpackEntity;
import com.seeds.game.enums.NftHeroTypeEnum;
import com.seeds.game.enums.NftRarityEnum;
import com.seeds.game.enums.NftTypeEnum;
import com.seeds.game.service.INftAttributeService;
import com.seeds.game.service.INftMarketOrderService;
import com.seeds.game.service.INftPublicBackpackService;
import com.seeds.game.dto.response.NftOfferResp;
import com.seeds.game.service.NftMarketPlaceService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import java.util.List;

@Service
public class NftMarketPlaceServiceImpl implements NftMarketPlaceService {

    @Autowired
    private INftMarketOrderService nftMarketOrderService;

    @Autowired
    private INftPublicBackpackService nftPublicBackpackService;

    @Autowired
    private INftAttributeService nftAttributeService;

    @Override
    public NftMarketPlaceDetailResp detail(Long id) {
        return null;
    }

    @Override
    public void fixedPriceShelf(NftFixedPriceShelfReq req) {

    }

    @Override
    public void britishAuctionShelf(NftBritishAuctionShelfReq req) {

    }

    @Override
    public void shelved(NftShelvedReq req) {

    }

    @Override
    public void makeOffer(NftMakeOfferReq req) {

    }

    @Override
    public void buySuccess(NftBuySuccessReq req) {

    }

    @Override
    public IPage<NftMarketPlaceSkinResp> skinQueryPage(NftMarketPlaceSkinPageReq skinQuery) {

        NftPublicBackpackEntity backpackEntity = nftPublicBackpackService.detailForTokenId(skinQuery.getTokenId());
        String NftImage = backpackEntity.getImage();

        LambdaQueryWrapper<NftAttributeEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(skinQuery.getName() != null, NftAttributeEntity::getName, skinQuery.getName())
                .or().eq(!StringUtils.isEmpty(skinQuery.getTokenId()), NftAttributeEntity::getTokenId,skinQuery.getTokenId())
                .eq(skinQuery.getGrade() != null, NftAttributeEntity::getGrade,skinQuery.getGrade())
                .eq(skinQuery.getDurability() != null, NftAttributeEntity::getDurability,skinQuery.getDurability())
                .eq(skinQuery.getRarity() != null, NftAttributeEntity::getRarity, NftRarityEnum.Common.getCode())
                .eq(skinQuery.getRarity() != null, NftAttributeEntity::getRarity, NftRarityEnum.Rare.getCode())
                .eq(skinQuery.getRarity() != null, NftAttributeEntity::getRarity, NftRarityEnum.Epic.getCode())
                .ge(skinQuery.getMinPrice() != null, NftAttributeEntity::getPrice, skinQuery.getMinPrice())
                .le(skinQuery.getMaxPrice() != null, NftAttributeEntity::getPrice, skinQuery.getMaxPrice())
                .eq(skinQuery.getHeroType() != null, NftAttributeEntity::getHeroType, NftHeroTypeEnum.DESTIN.getCode())
                .eq(skinQuery.getHeroType() != null, NftAttributeEntity::getHeroType, NftHeroTypeEnum.AILITH.getCode())
                .eq(skinQuery.getHeroType() != null, NftAttributeEntity::getHeroType, NftHeroTypeEnum.AILSA.getCode())
                .eq(skinQuery.getHeroType() != null, NftAttributeEntity::getHeroType, NftHeroTypeEnum.NELA.getCode())
                .eq(skinQuery.getHeroType() != null, NftAttributeEntity::getHeroType, NftHeroTypeEnum.CATHAL.getCode())
                .eq(skinQuery.getType() != null, NftAttributeEntity::getType, NftTypeEnum.skin.getCode())
                .orderByDesc(NftAttributeEntity::getPrice)
                .or().orderByAsc(NftAttributeEntity::getPrice)
                .or().orderByDesc(NftAttributeEntity::getGrade)
                .or().orderByAsc(NftAttributeEntity::getGrade)
                .or().orderByDesc(NftAttributeEntity::getDurability)
                .or().orderByAsc(NftAttributeEntity::getDurability)
                .or().orderByDesc(NftAttributeEntity::getRarity)
                .or().orderByAsc(NftAttributeEntity::getRarity)
                .orderByDesc(NftAttributeEntity::getCreatedAt);

        Page<NftAttributeEntity> page = new Page<>(skinQuery.getCurrent(), skinQuery.getSize());
        List<NftAttributeEntity> records = nftAttributeService.page(page, queryWrapper).getRecords();
        if (CollectionUtils.isEmpty(records)) {
            return page.convert(p -> null);
        }

        // 拼接#在tokenid前面，作为市场NFT的唯一编号
        String NftNumber = "#"+skinQuery.getTokenId();
        return page.convert(p -> {
            NftMarketPlaceSkinResp resp = new NftMarketPlaceSkinResp();
            BeanUtils.copyProperties(p, resp);
            resp.setNumber(NftNumber);
            resp.setImage(NftImage);
            return resp;
        });
    }

    @Override
    public IPage<NftMarketPlaceEqiupmentResp> equipQueryPage(NftMarketPlaceEquipPageReq equipQuery) {

        NftPublicBackpackEntity backpackEntity = nftPublicBackpackService.detailForTokenId(equipQuery.getTokenId());
        String NftImage = backpackEntity.getImage();

        LambdaQueryWrapper<NftAttributeEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(!StringUtils.isEmpty(equipQuery.getName()),NftAttributeEntity::getName ,equipQuery.getName())
                .or().eq(!StringUtils.isEmpty(equipQuery.getTokenId()), NftAttributeEntity::getTokenId,equipQuery.getTokenId())
                .eq(equipQuery.getGrade() != null, NftAttributeEntity::getGrade,equipQuery.getGrade())
                .eq(equipQuery.getDurability() != null, NftAttributeEntity::getDurability,equipQuery.getDurability())
                .eq(equipQuery.getRarity() != null, NftAttributeEntity::getRarity, NftRarityEnum.Common.getCode())
                .eq(equipQuery.getRarity() != null, NftAttributeEntity::getRarity, NftRarityEnum.Rare.getCode())
                .eq(equipQuery.getRarity() != null, NftAttributeEntity::getRarity, NftRarityEnum.Epic.getCode())
                .ge(equipQuery.getMinPrice() != null, NftAttributeEntity::getPrice, equipQuery.getMinPrice())
                .le(equipQuery.getMaxPrice() != null, NftAttributeEntity::getPrice, equipQuery.getMaxPrice())
                .eq(equipQuery.getHeroType() != null, NftAttributeEntity::getHeroType, NftHeroTypeEnum.DESTIN.getCode())
                .eq(equipQuery.getHeroType() != null, NftAttributeEntity::getHeroType, NftHeroTypeEnum.AILITH.getCode())
                .eq(equipQuery.getHeroType() != null, NftAttributeEntity::getHeroType, NftHeroTypeEnum.AILSA.getCode())
                .eq(equipQuery.getHeroType() != null, NftAttributeEntity::getHeroType, NftHeroTypeEnum.NELA.getCode())
                .eq(equipQuery.getHeroType() != null, NftAttributeEntity::getHeroType, NftHeroTypeEnum.CATHAL.getCode())
                .eq(equipQuery.getType() != null, NftAttributeEntity::getType, NftTypeEnum.equip.getCode())
                .orderByDesc(NftAttributeEntity::getPrice)
                .or().orderByAsc(NftAttributeEntity::getPrice)
                .or().orderByDesc(NftAttributeEntity::getGrade)
                .or().orderByAsc(NftAttributeEntity::getGrade)
                .or().orderByDesc(NftAttributeEntity::getDurability)
                .or().orderByAsc(NftAttributeEntity::getDurability)
                .or().orderByDesc(NftAttributeEntity::getRarity)
                .or().orderByAsc(NftAttributeEntity::getRarity)
                .orderByDesc(NftAttributeEntity::getCreatedAt);

        Page<NftAttributeEntity> page = new Page<>(equipQuery.getCurrent(), equipQuery.getSize());
        List<NftAttributeEntity> records = nftAttributeService.page(page, queryWrapper).getRecords();
        if (CollectionUtils.isEmpty(records)) {
            return page.convert(p -> null);
        }

        // 拼接#在tokenid前面，作为市场NFT的唯一编号
        String NftNumber = "#"+equipQuery.getTokenId();
        return page.convert(p -> {
            NftMarketPlaceEqiupmentResp resp = new NftMarketPlaceEqiupmentResp();
            BeanUtils.copyProperties(p, resp);
            resp.setNumber(NftNumber);
            resp.setImage(NftImage);
            return resp;
        });
    }

    @Override
    public NftMarketPlaceDetailViewResp view(NftMarketPlaceDetailViewReq req) {
        NftPublicBackpackEntity backpackEntity = nftPublicBackpackService.detailForTokenId(req.getTokenId());
        Long userId = backpackEntity.getUserId();

        // 获取当前登录的用户的id
        Long currentUserId = UserContext.getCurrentUserId();
        LambdaQueryWrapper<NftAttributeEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(!StringUtils.isEmpty(req.getTokenId()),NftAttributeEntity::getTokenId,req.getTokenId());
        NftAttributeEntity one = nftAttributeService.getOne(queryWrapper);

        // 第一视角，浏览量不变；第三视角，则浏览量+1
        if (one != null && !userId.equals(currentUserId)){
            // 属性表更新NFT浏览量
            LambdaUpdateWrapper<NftAttributeEntity> updateWrap = new UpdateWrapper<NftAttributeEntity>().lambda()
                    .setSql("`views`=`views`+1")
                    .eq(NftAttributeEntity::getTokenId,req.getTokenId());
            nftAttributeService.update(updateWrap);
        }
        NftAttributeEntity nftAttributeEntity = nftAttributeService.detailForTokenId(req.getTokenId());
        NftMarketPlaceDetailViewResp resp = new NftMarketPlaceDetailViewResp();
        resp.setViews(nftAttributeEntity.getViews());
        return resp;
    }

    @Override
    public NftOfferResp offerPage(PageReq req) {
        return null;
    }

    @Override
    public NftActivityResp activityPage(PageReq req) {
        return null;
    }

    @Override
    public void acceptOffer(NftAcceptOfferReq req) {

    }

    @Override
    public void cancelOffer(NftCancelOfferReq req) {

    }

}
