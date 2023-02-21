package com.seeds.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seeds.admin.dto.SysNFTAttrDto;
import com.seeds.admin.dto.SysNFTAutoIdDto;
import com.seeds.admin.dto.request.SysNftPicPageReq;
import com.seeds.admin.dto.response.SysNftPicResp;
import com.seeds.admin.entity.SysNftPicEntity;
import com.seeds.admin.mapper.SysNftPicMapper;
import com.seeds.admin.service.SysNftPicService;
import com.seeds.admin.utils.CsvUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class SysNftPicImpl extends ServiceImpl<SysNftPicMapper, SysNftPicEntity>implements SysNftPicService {

    @Override
    public IPage<SysNftPicResp> queryPage(SysNftPicPageReq req) {
        LambdaQueryWrapper<SysNftPicEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(!Objects.isNull(req.getCreatedAt()), SysNftPicEntity::getCreatedAt, req.getCreatedAt())
                .eq(!Objects.isNull(req.getSymbol()), SysNftPicEntity::getSymbol, req.getSymbol())
                .eq(!Objects.isNull(req.getAutoId()), SysNftPicEntity::getAutoId, req.getAutoId())
                .eq(!Objects.isNull(req.getConfId()), SysNftPicEntity::getConfId, req.getConfId())
                .eq(!Objects.isNull(req.getTokenAddress()), SysNftPicEntity::getTokenAddress, req.getTokenAddress());

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
                    BeanUtils.copyProperties(p, one);
                    batchUpdate.add(one);
                });
            }
        } else {
            List<SysNFTAutoIdDto> autoIdDtos = CsvUtils.getCsvData(file, SysNFTAutoIdDto.class);
            if (!CollectionUtils.isEmpty(autoIdDtos)) {
                autoIdDtos.forEach(p -> {
                    SysNftPicEntity one = this.getOne(new LambdaQueryWrapper<SysNftPicEntity>().eq(SysNftPicEntity::getPicName, p.getPictureName()));
                    BeanUtils.copyProperties(p, one);
                    batchUpdate.add(one);
                });
            }
        }
        // 批量更新属性
        this.updateBatchById(batchUpdate);
    }
}
