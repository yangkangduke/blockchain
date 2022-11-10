package com.seeds.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seeds.admin.dto.request.SysRandomCodeDetailPageReq;
import com.seeds.admin.dto.response.SysRandomCodeDetailResp;
import com.seeds.admin.entity.SysRandomCodeDetailEntity;
import com.seeds.admin.mapper.SysRandomCodeDetailMapper;
import com.seeds.admin.service.SysRandomCodeDetailService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;


/**
 * 随机码
 *
 * @author hang.yu
 * @date 2022/10/10
 */
@Service
public class SysRandomCodeDetailServiceImpl extends ServiceImpl<SysRandomCodeDetailMapper, SysRandomCodeDetailEntity> implements SysRandomCodeDetailService {

    @Override
    public IPage<SysRandomCodeDetailResp> queryPage(SysRandomCodeDetailPageReq query) {
        LambdaQueryWrapper<SysRandomCodeDetailEntity> queryWrap = new QueryWrapper<SysRandomCodeDetailEntity>().lambda()
                .eq(SysRandomCodeDetailEntity::getBatchNo, query.getBatchNo())
                .eq(query.getUseFlag() != null, SysRandomCodeDetailEntity::getUseFlag, query.getUseFlag())
                .orderByDesc(SysRandomCodeDetailEntity::getId);
        Page<SysRandomCodeDetailEntity> page = page(new Page<>(query.getCurrent(), query.getSize()), queryWrap);
        List<SysRandomCodeDetailEntity> records = page.getRecords();
        if (CollectionUtils.isEmpty(records)) {
            return page.convert(p -> null);
        }
        return page.convert(p -> {
            SysRandomCodeDetailResp resp = new SysRandomCodeDetailResp();
            BeanUtils.copyProperties(p, resp);
            return resp;
        });
    }

    @Override
    public List<SysRandomCodeDetailEntity> queryByBatchNo(String batchNo) {
        LambdaQueryWrapper<SysRandomCodeDetailEntity> query = new QueryWrapper<SysRandomCodeDetailEntity>().lambda()
                .eq(SysRandomCodeDetailEntity::getBatchNo, batchNo);
        return list(query);
    }

    @Override
    public SysRandomCodeDetailEntity queryByBatchNoAndCode(String batchNo, String code) {
        LambdaQueryWrapper<SysRandomCodeDetailEntity> query = new QueryWrapper<SysRandomCodeDetailEntity>().lambda()
                .eq(SysRandomCodeDetailEntity::getBatchNo, batchNo)
                .eq(SysRandomCodeDetailEntity::getCode, code);
        return getOne(query);
    }

    @Override
    public void removeByBatchNo(String batchNo) {
        LambdaQueryWrapper<SysRandomCodeDetailEntity> query = new QueryWrapper<SysRandomCodeDetailEntity>().lambda()
                .eq(SysRandomCodeDetailEntity::getBatchNo, batchNo);
        remove(query);
    }
}

