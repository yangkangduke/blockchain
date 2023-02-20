package com.seeds.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seeds.admin.dto.request.SysNftPicPageReq;
import com.seeds.admin.dto.response.SysNftPicResp;
import com.seeds.admin.entity.SysNftPicEntity;
import com.seeds.admin.mapper.SysNftPicMapper;
import com.seeds.admin.service.SysNftPicService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import java.util.List;
import java.util.Objects;

@Service
public class SysNftPicImpl extends ServiceImpl<SysNftPicMapper, SysNftPicEntity>implements SysNftPicService {

    @Override
    public IPage<SysNftPicResp> queryPage(SysNftPicPageReq req) {
        LambdaQueryWrapper<SysNftPicEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(!Objects.isNull(req.getCreatedAt()),SysNftPicEntity::getCreatedAt,req.getCreatedAt())
                .eq(!Objects.isNull(req.getSymbol()),SysNftPicEntity::getSymbol,req.getSymbol())
                .eq(!Objects.isNull(req.getAutoId()),SysNftPicEntity::getAutoId,req.getAutoId())
                .eq(!Objects.isNull(req.getConfId()),SysNftPicEntity::getConfId,req.getConfId())
                .eq(!Objects.isNull(req.getTokenAddress()),SysNftPicEntity::getTokenAddress,req.getTokenAddress());

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
}
