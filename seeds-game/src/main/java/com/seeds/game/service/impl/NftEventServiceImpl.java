package com.seeds.game.service.impl;

import cn.hutool.extra.cglib.CglibUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seeds.admin.enums.WhetherEnum;
import com.seeds.game.dto.request.internal.NftEventAddReq;
import com.seeds.game.dto.request.internal.NftEventPageReq;
import com.seeds.game.dto.response.NftEventEquipmentResp;
import com.seeds.game.dto.response.NftEventResp;
import com.seeds.game.dto.response.TypeNum;
import com.seeds.game.entity.NftEvent;
import com.seeds.game.entity.NftEventEquipment;
import com.seeds.game.enums.NFTEnumConstant;
import com.seeds.game.mapper.NftEventMapper;
import com.seeds.game.service.INftEventEquipmentService;
import com.seeds.game.service.INftEventService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <p>
 * nft通知 服务实现类
 * </p>
 *
 * @author hewei
 * @since 2023-03-23
 */
@Service
public class NftEventServiceImpl extends ServiceImpl<NftEventMapper, NftEvent> implements INftEventService {

    @Autowired
    private INftEventEquipmentService eventEquipmentService;

    @Override
    @Transactional
    public IPage<NftEventResp> getPage(NftEventPageReq req) {


        LambdaQueryWrapper<NftEvent> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Objects.nonNull(req.getType()), NftEvent::getType, req.getType())
                .eq(NftEvent::getUserId, req.getUserId());

        wrapper.last(!CollectionUtils.isEmpty(req.getSorts()), NftEventPageReq.getOrderByStatement(req.getSorts()));
        Page<NftEvent> page = new Page<>(req.getCurrent(), req.getSize());
        List<NftEvent> records = this.page(page, wrapper).getRecords();
        if (CollectionUtils.isEmpty(records)) {
            return page.convert(p -> null);
        }
        // 获取事件涉及的装备
        List<Long> eventIds = records.stream().map(p -> p.getId()).collect(Collectors.toList());
        Map<Long, List<NftEventEquipment>> map = eventEquipmentService.list(new LambdaQueryWrapper<NftEventEquipment>().in(NftEventEquipment::getEventId, eventIds))
                .stream().collect(Collectors.groupingBy(NftEventEquipment::getEventId));

        // 更新未读数（click设置为以点击）
        baseMapper.updateClick(req.getType(), req.getUserId());

        return page.convert(p -> {
            NftEventResp resp = new NftEventResp();
            BeanUtils.copyProperties(p, resp);
            List<NftEventEquipment> equipments = map.get(p.getId());
            if (!CollectionUtils.isEmpty(equipments)) {
                resp.setEventEquipments(CglibUtil.copyList(equipments, NftEventEquipmentResp::new));
            }
            return resp;
        });

    }

    @Override
    public Long getHandleFlag(Long userId) {
        return this.count(new LambdaQueryWrapper<NftEvent>().eq(NftEvent::getUserId, userId).eq(NftEvent::getClick, WhetherEnum.NO.value()));
    }

    @Override
    @Transactional
    public void toNft(NftEventAddReq req) {
        if (req.getType().equals(NFTEnumConstant.NFTEventType.COMPOUND.getCode())) {
            // 必须有一个是nft,且是托管状态 todo
        }
        // 记录toNFT请求
        NftEvent nftEvent = new NftEvent();
        BeanUtils.copyProperties(req, nftEvent);
        nftEvent.setStatus(NFTEnumConstant.NFTEventStatus.PENDING.getCode());
        nftEvent.setCreatedAt(System.currentTimeMillis());
        nftEvent.setCreatedBy(req.getUserId());
        nftEvent.setUpdatedAt(System.currentTimeMillis());
        nftEvent.setUpdatedBy(req.getUserId());
        this.save(nftEvent);
        List<NftEventEquipment> equipments = CglibUtil.copyList(req.getEquipments(), NftEventEquipment::new);

        equipments.forEach(p -> {
            p.setEventId(nftEvent.getId());
        });
        eventEquipmentService.saveBatch(equipments);
        // todo 生成JSON文件

    }

    @Override
    public Boolean delete(Long id) {
        return this.removeById(id);
    }

    @Override
    public Boolean cancel(Long id) {

        NftEvent nftEvent = new NftEvent();
        nftEvent.setId(id);
        nftEvent.setStatus(NFTEnumConstant.NFTEventStatus.CANCELLED.getCode());

        // todo 通知游戏方事件取消

        return this.updateById(nftEvent);
    }

    @Override
    public List<TypeNum> getTypeNum(Long userId) {
        // 获取各个类型的数量
        List<TypeNum> typeCount = baseMapper.getTypeCount(userId);
        return typeCount;
    }

}
