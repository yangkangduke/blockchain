package com.seeds.admin.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seeds.admin.dto.SysNFTAttrDto;
import com.seeds.admin.dto.SysNFTAttrJSONDto;
import com.seeds.admin.dto.SysNFTAutoIdDto;
import com.seeds.admin.dto.request.SysNftPicAttributeModifyReq;
import com.seeds.admin.dto.request.SysNftPicPageReq;
import com.seeds.admin.dto.response.SysNftPicResp;
import com.seeds.admin.entity.SysNftPicEntity;
import com.seeds.admin.enums.NftAttrEnum;
import com.seeds.admin.mapper.SysNftPicMapper;
import com.seeds.admin.mq.producer.KafkaProducer;
import com.seeds.admin.service.SysNftPicService;
import com.seeds.admin.utils.CsvUtils;
import com.seeds.common.constant.mq.KafkaTopic;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class SysNftPicImpl extends ServiceImpl<SysNftPicMapper, SysNftPicEntity> implements SysNftPicService {

    @Autowired
    private KafkaProducer kafkaProducer;

    @Override
    public IPage<SysNftPicResp> queryPage(SysNftPicPageReq req) {
        LambdaQueryWrapper<SysNftPicEntity> queryWrapper = new LambdaQueryWrapper<>();
        Long start = 0L;
        Long end = 0L;
        if (!Objects.isNull(req.getQueryTime())) {
            DateTime dateTime = DateUtil.parseDate(req.getQueryTime());
            start = DateUtil.beginOfDay(dateTime).getTime();
            end = DateUtil.endOfDay(dateTime).getTime();
        }
        queryWrapper.between(!Objects.isNull(req.getQueryTime()), SysNftPicEntity::getCreatedAt, start, end)
                .eq(!Objects.isNull(req.getRarity()), SysNftPicEntity::getRarity, req.getRarity())
                .eq(!Objects.isNull(req.getFeature()), SysNftPicEntity::getFeature, req.getFeature())
                .eq(!Objects.isNull(req.getColor()), SysNftPicEntity::getColor, req.getColor())
                .eq(!Objects.isNull(req.getAccessories()), SysNftPicEntity::getAccessories, req.getAccessories())
                .eq(!Objects.isNull(req.getDecorate()), SysNftPicEntity::getDecorate, req.getDecorate())
                .eq(!Objects.isNull(req.getOther()), SysNftPicEntity::getOther, req.getOther())
                .eq(!Objects.isNull(req.getHero()), SysNftPicEntity::getHero, req.getHero())
                .eq(!Objects.isNull(req.getSkin()), SysNftPicEntity::getSkin, req.getSkin())
                .eq(!Objects.isNull(req.getAutoId()), SysNftPicEntity::getAutoId, req.getAutoId())
                .eq(!Objects.isNull(req.getConfId()), SysNftPicEntity::getConfId, req.getConfId())
                .eq(!Objects.isNull(req.getTokenAddress()), SysNftPicEntity::getTokenAddress, req.getTokenAddress())
                .orderByDesc(SysNftPicEntity::getCreatedAt);

        Page<SysNftPicEntity> page = new Page<>(req.getCurrent(), req.getSize());
        List<SysNftPicEntity> records = this.page(page, queryWrapper).getRecords();

        if (CollectionUtils.isEmpty(records)) {
            return page.convert(p -> null);
        }
        return page.convert(p -> {
            SysNftPicResp resp = new SysNftPicResp();
            BeanUtils.copyProperties(p, resp);
            return resp;
        });
    }

    @Override
    public void upload(MultipartFile file, Integer type) {
        List<SysNftPicEntity> batchUpdate = new ArrayList<>();

        // 解析CSV文件
        if (type == 1) {
            List<SysNFTAttrDto> sysNFTAttrDtos = CsvUtils.getCsvData(file, SysNFTAttrDto.class);
            if (!CollectionUtils.isEmpty(sysNFTAttrDtos)) {
                sysNFTAttrDtos.forEach(p -> {
                    SysNftPicEntity one = this.getOne(new LambdaQueryWrapper<SysNftPicEntity>().eq(SysNftPicEntity::getPicName, p.getPictureName()));
                    if (one.getPicName().equals(p.getPictureName())) {
                        BeanUtils.copyProperties(p, one);
                        one.setDescription(p.getDesc());
                        one.setUpdatedAt(System.currentTimeMillis());
                        batchUpdate.add(one);
                    }
                });
            }
        } else {
            List<SysNFTAutoIdDto> autoIdDtos = CsvUtils.getCsvData(file, SysNFTAutoIdDto.class);
            if (!CollectionUtils.isEmpty(autoIdDtos)) {
                autoIdDtos.forEach(p -> {
                    SysNftPicEntity one = this.getOne(new LambdaQueryWrapper<SysNftPicEntity>().eq(SysNftPicEntity::getPicName, p.getPictureName()));
                    if (one.getPicName().equals(p.getPictureName())) {
                        BeanUtils.copyProperties(p, one);
                        one.setUpdatedAt(System.currentTimeMillis());
                        batchUpdate.add(one);
                    }
                });
            }
        }
        // 批量更新属性
        this.updateBatchById(batchUpdate);

        // 屬性更新成功消息
        kafkaProducer.send(KafkaTopic.NFT_PIC_ATTR_UPDATE_SUCCESS, JSONUtil.toJsonStr(batchUpdate.stream().map(p -> p.getId()).collect(Collectors.toList())));
    }

    @Override
    public String getAttr(Long id) {

        SysNftPicEntity entity = this.getById(id);
        SysNFTAttrJSONDto sysNFTAttrJSONDto = this.handelAttr(entity);
        return JSONUtil.toJsonStr(sysNFTAttrJSONDto);
    }

    public SysNFTAttrJSONDto handelAttr(SysNftPicEntity entity) {
        SysNFTAttrJSONDto jsonDto = new SysNFTAttrJSONDto();
        jsonDto.setName(entity.getName());
        jsonDto.setSymbol(entity.getSymbol());
        jsonDto.setDescription(entity.getDescription());
        jsonDto.setImage(entity.getPicName());

        ArrayList<SysNFTAttrJSONDto.Attributes> attributesList = new ArrayList<>();
        if (!StrUtil.isEmpty(entity.getRarity())) {
            SysNFTAttrJSONDto.Attributes attributes = new SysNFTAttrJSONDto.Attributes();
            attributes.setTraitType(NftAttrEnum.RARITY.getName());
            attributes.setValue(entity.getRarity());
            attributesList.add(attributes);
        }
        if (!StrUtil.isEmpty(entity.getFeature())) {
            SysNFTAttrJSONDto.Attributes attributes = new SysNFTAttrJSONDto.Attributes();
            attributes.setTraitType(NftAttrEnum.FEATURE.getName());
            attributes.setValue(entity.getFeature());
            attributesList.add(attributes);
        }
        if (!StrUtil.isEmpty(entity.getColor())) {
            SysNFTAttrJSONDto.Attributes attributes = new SysNFTAttrJSONDto.Attributes();
            attributes.setTraitType(NftAttrEnum.COLOR.getName());
            attributes.setValue(entity.getColor());
            attributesList.add(attributes);
        }
        if (!StrUtil.isEmpty(entity.getAccessories())) {
            SysNFTAttrJSONDto.Attributes attributes = new SysNFTAttrJSONDto.Attributes();
            attributes.setTraitType(NftAttrEnum.ACCESSORIES.getName());
            attributes.setValue(entity.getAccessories());
            attributesList.add(attributes);
        }
        if (!StrUtil.isEmpty(entity.getDecorate())) {
            SysNFTAttrJSONDto.Attributes attributes = new SysNFTAttrJSONDto.Attributes();
            attributes.setTraitType(NftAttrEnum.DECORATE.getName());
            attributes.setValue(entity.getDecorate());
            attributesList.add(attributes);
        }
        if (!StrUtil.isEmpty(entity.getOther())) {
            SysNFTAttrJSONDto.Attributes attributes = new SysNFTAttrJSONDto.Attributes();
            attributes.setTraitType(NftAttrEnum.OTHER.getName());
            attributes.setValue(entity.getOther());
            attributesList.add(attributes);
        }
        if (!StrUtil.isEmpty(entity.getHero())) {
            SysNFTAttrJSONDto.Attributes attributes = new SysNFTAttrJSONDto.Attributes();
            attributes.setTraitType(NftAttrEnum.HERO.getName());
            attributes.setValue(entity.getHero());
            attributesList.add(attributes);
        }
        if (!StrUtil.isEmpty(entity.getSkin())) {
            SysNFTAttrJSONDto.Attributes attributes = new SysNFTAttrJSONDto.Attributes();
            attributes.setTraitType(NftAttrEnum.SKIN.getName());
            attributes.setValue(entity.getSkin());
            attributesList.add(attributes);
        }
        jsonDto.setAttributes(attributesList);
        SysNFTAttrJSONDto.Properties.Files files = new SysNFTAttrJSONDto.Properties.Files();
        files.setType("image/png");
        files.setUri(entity.getPicName());
        ArrayList<SysNFTAttrJSONDto.Properties.Files> fileList = new ArrayList<>();
        fileList.add(files);
        SysNFTAttrJSONDto.Properties properties = new SysNFTAttrJSONDto.Properties();
        properties.setFiles(fileList);
        jsonDto.setProperties(properties);
        return jsonDto;
    }

    @Override
    public void updateAttribute(SysNftPicAttributeModifyReq req) {
        SysNftPicEntity sysNftPicEntity = new SysNftPicEntity();
        BeanUtils.copyProperties(req, sysNftPicEntity);
        this.updateById(sysNftPicEntity);

        // 屬性更新成功消息
        kafkaProducer.send(KafkaTopic.NFT_PIC_ATTR_UPDATE_SUCCESS, JSONUtil.toJsonStr(CollectionUtil.newArrayList(req.getId())));
    }
}
