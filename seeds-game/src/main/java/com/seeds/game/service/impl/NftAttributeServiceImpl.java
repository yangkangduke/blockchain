package com.seeds.game.service.impl;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seeds.common.web.context.UserContext;
import com.seeds.game.dto.request.NftMarketPlaceDetailViewReq;
import com.seeds.game.dto.request.NftMarketPlaceEquipPageReq;
import com.seeds.game.dto.request.NftMarketPlaceSkinPageReq;
import com.seeds.game.dto.response.NftMarketPlaceDetailViewResp;
import com.seeds.game.dto.response.NftMarketPlaceEqiupmentResp;
import com.seeds.game.dto.response.NftMarketPlaceSkinResp;
import com.seeds.game.entity.NftAttributeEntity;
import com.seeds.game.entity.NftMarketOrderEntity;
import com.seeds.game.entity.NftPublicBackpackEntity;
import com.seeds.game.enums.NftHeroTypeEnum;
import com.seeds.game.enums.NftTypeEnum;
import com.seeds.game.mapper.NftAttributeMapper;
import com.seeds.game.service.INftAttributeService;
import com.seeds.game.service.INftMarketOrderService;
import com.seeds.game.service.INftPublicBackpackService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;


@Service
public class NftAttributeServiceImpl extends ServiceImpl<NftAttributeMapper, NftAttributeEntity>implements INftAttributeService {

    @Autowired
    private INftPublicBackpackService nftPublicBackpackService;

    @Autowired
    private INftMarketOrderService nftMarketOrderService;


    @Override
    public IPage<NftMarketPlaceSkinResp> skinQueryPage(NftMarketPlaceSkinPageReq skinQuery) {

        Long start = 0L;
        Long end = 0L;
        if (!Objects.isNull(skinQuery.getListedTime())) {
            DateTime dateTime = DateUtil.parseDate(skinQuery.getListedTime());
            start = DateUtil.beginOfDay(dateTime).getTime();
            end = DateUtil.endOfDay(dateTime).getTime();
        }
        // 从Order表中获取price
        NftMarketOrderEntity orderEntity = nftMarketOrderService.detailForTokenId(Long.valueOf(skinQuery.getTokenId()));
        BigDecimal price = orderEntity.getPrice();

        // 从背包表获取NFT
        NftPublicBackpackEntity backpackEntity = nftPublicBackpackService.detailForTokenId(skinQuery.getTokenId());

        // NFT 名称
        String name = backpackEntity.getName();

        LambdaQueryWrapper<NftAttributeEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.between(!Objects.isNull(skinQuery.getListedTime()), NftAttributeEntity::getCreatedAt, start, end)
               // .like(skinQuery.getName() != null, name, skinQuery.getName())
                .or().eq(!StringUtils.isEmpty(skinQuery.getTokenId()), NftAttributeEntity::getTokenId,skinQuery.getTokenId())
                .eq(skinQuery.getGrade() != null, NftAttributeEntity::getGrade,skinQuery.getGrade())
                .eq(skinQuery.getDurability() != null, NftAttributeEntity::getDurability,skinQuery.getDurability())
                .eq(skinQuery.getRarity() != null, NftAttributeEntity::getRarity,skinQuery.getRarity())
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
                .or().orderByDesc(NftAttributeEntity::getFavorite)
                .or().orderByAsc(NftAttributeEntity::getFavorite)
                .orderByDesc(NftAttributeEntity::getCreatedAt);

        Page<NftAttributeEntity> page = new Page<>(skinQuery.getCurrent(), skinQuery.getSize());
        List<NftAttributeEntity> records = this.page(page, queryWrapper).getRecords();
        if (CollectionUtils.isEmpty(records)) {
            return page.convert(p -> null);
        }


        // 拼接#在tokenid前面，作为市场NFT的唯一编号
        String NftNumber = "#"+skinQuery.getTokenId();
        return page.convert(p -> {
            NftMarketPlaceSkinResp resp = new NftMarketPlaceSkinResp();
            BeanUtils.copyProperties(p, resp);
            resp.setIdentifier(NftNumber);
            resp.setName(name);
            resp.setImage(backpackEntity.getImage());
            resp.setPrice(price);
            return resp;
        });
    }

    @Override
    public IPage<NftMarketPlaceEqiupmentResp> equipQueryPage(NftMarketPlaceEquipPageReq equipQuery) {
        Long start = 0L;
        Long end = 0L;
        if (!Objects.isNull(equipQuery.getListedTime())) {
            DateTime dateTime = DateUtil.parseDate(equipQuery.getListedTime());
            start = DateUtil.beginOfDay(dateTime).getTime();
            end = DateUtil.endOfDay(dateTime).getTime();
        }

        // 获取price
        NftMarketOrderEntity orderEntity = nftMarketOrderService.detailForTokenId(Long.valueOf(equipQuery.getTokenId()));
        BigDecimal price = orderEntity.getPrice();

        // 从背包表获取NFT
        NftPublicBackpackEntity backpackEntity = nftPublicBackpackService.detailForTokenId(equipQuery.getTokenId());

        // NFT 名称
        String nftName = backpackEntity.getName();

        LambdaQueryWrapper<NftAttributeEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.between(!Objects.isNull(equipQuery.getListedTime()), NftAttributeEntity::getCreatedAt, start, end)
               // .like(!StringUtils.isEmpty(equipQuery.getName()), ,equipQuery.getName())
                .or().eq(!StringUtils.isEmpty(equipQuery.getTokenId()), NftAttributeEntity::getTokenId,equipQuery.getTokenId())
                .eq(equipQuery.getGrade() != null, NftAttributeEntity::getGrade,equipQuery.getGrade())
                .eq(equipQuery.getDurability() != null, NftAttributeEntity::getDurability,equipQuery.getDurability())
                .eq(equipQuery.getRarity() != null, NftAttributeEntity::getRarity,equipQuery.getRarity())
                .ge(equipQuery.getMinPrice() != null, NftAttributeEntity::getPrice, equipQuery.getMinPrice())
                .le(equipQuery.getMaxPrice() != null, NftAttributeEntity::getPrice, equipQuery.getMaxPrice())
                .eq(equipQuery.getHeroType() != null, NftAttributeEntity::getHeroType, NftHeroTypeEnum.DESTIN.getCode())
                .eq(equipQuery.getHeroType() != null, NftAttributeEntity::getHeroType, NftHeroTypeEnum.AILITH.getCode())
                .eq(equipQuery.getHeroType() != null, NftAttributeEntity::getHeroType, NftHeroTypeEnum.AILSA.getCode())
                .eq(equipQuery.getHeroType() != null, NftAttributeEntity::getHeroType, NftHeroTypeEnum.NELA.getCode())
                .eq(equipQuery.getHeroType() != null, NftAttributeEntity::getHeroType, NftHeroTypeEnum.CATHAL.getCode())
                .eq(equipQuery.getType() != null, NftAttributeEntity::getType, NftTypeEnum.equip.getCode())
                .or().orderByDesc(NftAttributeEntity::getGrade)
                .or().orderByAsc(NftAttributeEntity::getGrade)
                .or().orderByDesc(NftAttributeEntity::getDurability)
                .or().orderByAsc(NftAttributeEntity::getDurability)
                .or().orderByDesc(NftAttributeEntity::getRarity)
                .or().orderByAsc(NftAttributeEntity::getRarity)
                .or().orderByDesc(NftAttributeEntity::getFavorite)
                .or().orderByAsc(NftAttributeEntity::getFavorite)
                .orderByDesc(NftAttributeEntity::getCreatedAt);

        Page<NftAttributeEntity> page = new Page<>(equipQuery.getCurrent(), equipQuery.getSize());
        List<NftAttributeEntity> records = this.page(page, queryWrapper).getRecords();
        if (CollectionUtils.isEmpty(records)) {
            return page.convert(p -> null);
        }

        // 拼接#在tokenid前面，作为市场NFT的唯一编号
        String NftNumber = "#"+equipQuery.getTokenId();
        return page.convert(p -> {
            NftMarketPlaceEqiupmentResp resp = new NftMarketPlaceEqiupmentResp();
            BeanUtils.copyProperties(p, resp);
            resp.setIdentifier(NftNumber);
            resp.setName(nftName);
            resp.setImage(backpackEntity.getImage());
            resp.setPrice(price);
            return resp;
        });
    }

    @Override
    public NftMarketPlaceDetailViewResp view(NftMarketPlaceDetailViewReq req) {
        return null;
    }

   /* @Override
    public NftMarketPlaceDetailViewResp view(NftMarketPlaceDetailViewReq req) {
        NftPublicBackpackEntity backpackEntity = nftPublicBackpackService.detailForTokenId(req.getTokenId());
        Long userId = backpackEntity.getUserId();

        // 获取当前登录的用户的id
        Long currentUserId = UserContext.getCurrentUserId();
        LambdaQueryWrapper<NftAttributeEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(!StringUtils.isEmpty(req.getTokenId()),NftAttributeEntity::getTokenId,req.getTokenId());
        NftAttributeEntity one = this.getOne(queryWrapper);

        // 第一视角，浏览量不变；第三视角，则浏览量+1
        if (one != null && !userId.equals(currentUserId)){
            // 属性表更新NFT浏览量
            LambdaUpdateWrapper<NftAttributeEntity> updateWrap = new UpdateWrapper<NftAttributeEntity>().lambda()
                    .setSql("`views`=`views`+1")
                    .eq(NftAttributeEntity::getTokenId,req.getTokenId());
            nftAttributeService.update(updateWrap);
        }
        NftAttributeEntity attributeEntity = new NftAttributeEntity();
        NftMarketPlaceDetailViewResp resp = new NftMarketPlaceDetailViewResp();
        resp.setViews(attributeEntity.getViews());
        return resp;
    }*/
}
