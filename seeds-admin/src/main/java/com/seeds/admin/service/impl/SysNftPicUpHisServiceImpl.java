package com.seeds.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seeds.admin.dto.request.PageReq;
import com.seeds.admin.dto.response.SysNftPicUpHisResp;
import com.seeds.admin.entity.SysNftPicUpHisEntity;
import com.seeds.admin.mapper.SysNftPicUpHisMapper;
import com.seeds.admin.service.SysNftPicUpHisService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * NFT内容上传记录服务实现类
 *
 * @author yang.deng
 * @date 2023/2/20
 */

@Service
public class SysNftPicUpHisServiceImpl extends ServiceImpl<SysNftPicUpHisMapper, SysNftPicUpHisEntity>implements SysNftPicUpHisService {

    @Override
    public IPage<SysNftPicUpHisResp> queryPage(PageReq req) {
        LambdaQueryWrapper<SysNftPicUpHisEntity> queryWrapper = new QueryWrapper<SysNftPicUpHisEntity>().lambda()
                .orderByDesc(SysNftPicUpHisEntity::getCreatedAt);

        Page<SysNftPicUpHisEntity> page = new Page<>(req.getCurrent(), req.getSize());
        List<SysNftPicUpHisEntity> records = this.page(page, queryWrapper).getRecords();
        if (CollectionUtils.isEmpty(records)) {
            return page.convert(p -> null);
        }
        return page.convert(p -> {
            SysNftPicUpHisResp resp = new SysNftPicUpHisResp();
            BeanUtils.copyProperties(p, resp);
            return resp;
        });
    }
}
