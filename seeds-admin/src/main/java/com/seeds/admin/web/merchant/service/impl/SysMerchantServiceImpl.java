package com.seeds.admin.web.merchant.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seeds.admin.dto.merchant.request.SysMerchantAddReq;
import com.seeds.admin.dto.merchant.request.SysMerchantModifyReq;
import com.seeds.admin.dto.merchant.request.SysMerchantPageReq;
import com.seeds.admin.dto.merchant.response.SysMerchantResp;
import com.seeds.admin.dto.sys.request.SysUserAddReq;
import com.seeds.admin.entity.merchant.SysMerchantEntity;
import com.seeds.admin.enums.WhetherEnum;
import com.seeds.admin.web.merchant.mapper.SysMerchantMapper;
import com.seeds.admin.web.merchant.service.SysMerchantService;
import com.seeds.admin.web.sys.service.SysUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;


/**
 * 系统商家
 *
 * @author hang.yu
 * @date 2022/7/19
 */
@Service
public class SysMerchantServiceImpl extends ServiceImpl<SysMerchantMapper, SysMerchantEntity> implements SysMerchantService {

    @Autowired
    private SysUserService sysUserService;

    @Override
    public List<SysMerchantResp> queryList() {
        QueryWrapper<SysMerchantEntity> query = new QueryWrapper<>();
        query.eq("delete_flag", WhetherEnum.NO.value());
        List<SysMerchantEntity> list = list(query);
        return convertToResp(list);
    }

    @Override
    public IPage<SysMerchantResp> queryPage(SysMerchantPageReq query) {
        QueryWrapper<SysMerchantEntity> queryWrap = new QueryWrapper<>();
        queryWrap.likeRight(StringUtils.isNotBlank(query.getNameOrMobile()), "name", query.getNameOrMobile())
                .or().likeRight(StringUtils.isNotBlank(query.getNameOrMobile()), "mobile", query.getNameOrMobile());
        queryWrap.eq("delete_flag", WhetherEnum.NO.value());
        Page<SysMerchantEntity> page = new Page<>(query.getCurrent(), query.getSize());
        List<SysMerchantEntity> records = page(page, queryWrap).getRecords();
        if (CollectionUtils.isEmpty(records)) {
            return page.convert(p -> null);
        }
        Set<Long> userIds = records.stream().map(SysMerchantEntity::getLeaderId).filter(Objects::nonNull).collect(Collectors.toSet());
        Map<Long, String> userMap = sysUserService.queryNameMapByIds(userIds);
        return page.convert(p -> {
            SysMerchantResp resp = new SysMerchantResp();
            BeanUtils.copyProperties(p, resp);
            resp.setLeaderName(userMap.get(p.getLeaderId()));
            return resp;
        });
    }

    @Override
    public void add(SysMerchantAddReq req) {
        if (req.getLeaderId() == null) {
            // 添加商家用户
            SysUserAddReq addReq = new SysUserAddReq();
            sysUserService.add(addReq);
        }
    }

    @Override
    public SysMerchantResp detail(Long id) {
        return null;
    }

    @Override
    public void modify(SysMerchantModifyReq req) {

    }

    @Override
    public void delete(Long id) {

    }

    private List<SysMerchantResp> convertToResp(List<SysMerchantEntity> list){
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyList();
        }
        List<SysMerchantResp> respList = new ArrayList<>();
        list.forEach(p -> {
            SysMerchantResp resp = new SysMerchantResp();
            BeanUtils.copyProperties(p, resp);
            respList.add(resp);
        });
        return respList;
    }
}