package com.seeds.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seeds.admin.dto.request.SysLogPageReq;
import com.seeds.admin.dto.response.SysLogResp;
import com.seeds.admin.entity.SysLogEntity;
import com.seeds.admin.mapper.SysLogMapper;
import com.seeds.admin.service.SysLogService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @author: hewei
 * @date 2022/7/26
 */
@Service
public class SysLogServiceImpl extends ServiceImpl<SysLogMapper, SysLogEntity> implements SysLogService {

    @Override
    public IPage<SysLogResp> queryPage(SysLogPageReq req) {
        LambdaQueryWrapper<SysLogEntity> wrapper = new LambdaQueryWrapper<>();

        wrapper.like(StringUtils.isNotBlank(req.getOperation()), SysLogEntity::getOperation, req.getOperation())
                .like(StringUtils.isNotBlank(req.getOperator()), SysLogEntity::getOperatorName, req.getOperator());

        Page<SysLogEntity> page = new Page<>(req.getCurrent(), req.getSize());
        List<SysLogEntity> records = page(page, wrapper).getRecords();
        if (CollectionUtils.isEmpty(records)) {
            return page.convert(p -> null);
        }

        return page.convert(p -> {
            SysLogResp resp = new SysLogResp();
            BeanUtils.copyProperties(p, resp);
            return resp;
        });
    }
}
