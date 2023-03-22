package com.seeds.game.service.impl;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seeds.admin.entity.SysNftPicEntity;
import com.seeds.admin.enums.WhetherEnum;
import com.seeds.game.dto.request.NftMarketPlaceEquipPageReq;
import com.seeds.game.dto.request.NftMarketPlaceSkinPageReq;
import com.seeds.game.dto.response.NftMarketPlaceEqiupmentResp;
import com.seeds.game.dto.response.NftMarketPlaceSkinResp;
import com.seeds.game.entity.NftMarketPlaceEntity;
import com.seeds.game.enums.NftTypeEnum;
import com.seeds.game.mapper.NftMarketPlaceMapper;
import com.seeds.game.service.INftMarketPlaceService;
import com.seeds.game.service.INftPublicBackpackService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import java.util.List;
import java.util.Objects;


@Service
public class NftMarketPlaceServiceImpl extends ServiceImpl<NftMarketPlaceMapper, NftMarketPlaceEntity>implements INftMarketPlaceService {

    @Autowired
    private INftPublicBackpackService nftPublicBackpackService;

    @Override
    public IPage<NftMarketPlaceSkinResp> skinQueryPage(NftMarketPlaceSkinPageReq skinQuery) {

        Long start = 0L;
        Long end = 0L;
        if (!Objects.isNull(skinQuery.getListedTime())) {
            DateTime dateTime = DateUtil.parseDate(skinQuery.getListedTime());
            start = DateUtil.beginOfDay(dateTime).getTime();
            end = DateUtil.endOfDay(dateTime).getTime();
        }

        LambdaQueryWrapper<NftMarketPlaceEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.between(!Objects.isNull(skinQuery.getListedTime()), NftMarketPlaceEntity::getCreatedAt, start, end)
                .like(!StringUtils.isEmpty(skinQuery.getName()),NftMarketPlaceEntity::getName,skinQuery.getName())
                .or().eq(!StringUtils.isEmpty(skinQuery.getTokenId()),NftMarketPlaceEntity::getTokenId,skinQuery.getTokenId())
                .eq(skinQuery.getStatus() != null,NftMarketPlaceEntity::getStatus,skinQuery.getStatus())
                .eq(skinQuery.getGrade() != null,NftMarketPlaceEntity::getGrade,skinQuery.getGrade())
                .eq(skinQuery.getDurability() != null,NftMarketPlaceEntity::getDurability,skinQuery.getDurability())
                .eq(skinQuery.getRarity() != null,NftMarketPlaceEntity::getRarity,skinQuery.getRarity())
                .ge(skinQuery.getMinPrice() != null, NftMarketPlaceEntity::getPrice, skinQuery.getMinPrice())
                .le(skinQuery.getMaxPrice() != null, NftMarketPlaceEntity::getPrice, skinQuery.getMaxPrice())
                .eq(skinQuery.getType() != null,NftMarketPlaceEntity::getType, NftTypeEnum.skin.getCode())
                .eq(skinQuery.getIsShelf() != null,NftMarketPlaceEntity::getIsShelf, WhetherEnum.YES.value())
                .orderByDesc(NftMarketPlaceEntity::getPrice)
                .or().orderByDesc(NftMarketPlaceEntity::getGrade)
                .or().orderByAsc(NftMarketPlaceEntity::getGrade)
                .or().orderByDesc(NftMarketPlaceEntity::getDurability)
                .or().orderByAsc(NftMarketPlaceEntity::getDurability)
                .or().orderByDesc(NftMarketPlaceEntity::getRarity)
                .or().orderByAsc(NftMarketPlaceEntity::getRarity)
                .or().orderByDesc(NftMarketPlaceEntity::getFavorite)
                .or().orderByAsc(NftMarketPlaceEntity::getFavorite)
                .or().orderByAsc(NftMarketPlaceEntity::getWinRate)
                .or().orderByDesc(NftMarketPlaceEntity::getWinRate)
                .orderByDesc(NftMarketPlaceEntity::getCreatedAt);

        Page<NftMarketPlaceEntity> page = new Page<>(skinQuery.getCurrent(), skinQuery.getSize());
        List<NftMarketPlaceEntity> records = this.page(page, queryWrapper).getRecords();
        if (CollectionUtils.isEmpty(records)) {
            return page.convert(p -> null);
        }


        // 拼接#在tokenid前面，作为市场NFT的唯一编号
        String NftNumber = "#"+skinQuery.getTokenId();
        return page.convert(p -> {
            NftMarketPlaceSkinResp resp = new NftMarketPlaceSkinResp();
            BeanUtils.copyProperties(p, resp);
            resp.setIdentifier(NftNumber);
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

        LambdaQueryWrapper<NftMarketPlaceEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.between(!Objects.isNull(equipQuery.getListedTime()), NftMarketPlaceEntity::getCreatedAt, start, end)
                .like(!StringUtils.isEmpty(equipQuery.getName()),NftMarketPlaceEntity::getName,equipQuery.getName())
                .or().eq(!StringUtils.isEmpty(equipQuery.getTokenId()),NftMarketPlaceEntity::getTokenId,equipQuery.getTokenId())
                .eq(equipQuery.getStatus() != null,NftMarketPlaceEntity::getStatus,equipQuery.getStatus())
                .eq(equipQuery.getGrade() != null,NftMarketPlaceEntity::getGrade,equipQuery.getGrade())
                .eq(equipQuery.getDurability() != null,NftMarketPlaceEntity::getDurability,equipQuery.getDurability())
                .eq(equipQuery.getRarity() != null,NftMarketPlaceEntity::getRarity,equipQuery.getRarity())
                .ge(equipQuery.getMinPrice() != null, NftMarketPlaceEntity::getPrice, equipQuery.getMinPrice())
                .le(equipQuery.getMaxPrice() != null, NftMarketPlaceEntity::getPrice, equipQuery.getMaxPrice())
                .eq(equipQuery.getType() != null,NftMarketPlaceEntity::getType, NftTypeEnum.equip.getCode())
                .eq(equipQuery.getIsShelf() != null,NftMarketPlaceEntity::getIsShelf, WhetherEnum.YES.value())
                .orderByDesc(NftMarketPlaceEntity::getPrice)
                .or().orderByDesc(NftMarketPlaceEntity::getGrade)
                .or().orderByAsc(NftMarketPlaceEntity::getGrade)
                .or().orderByDesc(NftMarketPlaceEntity::getDurability)
                .or().orderByAsc(NftMarketPlaceEntity::getDurability)
                .or().orderByDesc(NftMarketPlaceEntity::getRarity)
                .or().orderByAsc(NftMarketPlaceEntity::getRarity)
                .or().orderByDesc(NftMarketPlaceEntity::getFavorite)
                .or().orderByAsc(NftMarketPlaceEntity::getFavorite)
                .or().orderByAsc(NftMarketPlaceEntity::getWinRate)
                .or().orderByDesc(NftMarketPlaceEntity::getWinRate)
                .orderByDesc(NftMarketPlaceEntity::getCreatedAt);

        Page<NftMarketPlaceEntity> page = new Page<>(equipQuery.getCurrent(), equipQuery.getSize());
        List<NftMarketPlaceEntity> records = this.page(page, queryWrapper).getRecords();
        if (CollectionUtils.isEmpty(records)) {
            return page.convert(p -> null);
        }

        // 拼接#在tokenid前面，作为市场NFT的唯一编号
        String NftNumber = "#"+equipQuery.getTokenId();
        return page.convert(p -> {
            NftMarketPlaceEqiupmentResp resp = new NftMarketPlaceEqiupmentResp();
            BeanUtils.copyProperties(p, resp);
            resp.setIdentifier(NftNumber);
            return resp;
        });
    }
}
