package com.seeds.game.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seeds.common.utils.RelativeDateFormat;
import com.seeds.game.dto.request.NftActivityPageReq;
import com.seeds.game.dto.response.NftActivityResp;
import com.seeds.game.entity.NftActivity;
import com.seeds.game.enums.NftActivityEnum;
import com.seeds.game.mapper.NftActivityMapper;
import com.seeds.game.service.INftActivityService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * nft活动记录
 * </p>
 *
 * @author hang.yu
 * @since 2023-03-28
 */
@Service
public class NftActivityServiceImpl extends ServiceImpl<NftActivityMapper, NftActivity> implements INftActivityService {

    @Override
    public IPage<NftActivityResp> queryPage(NftActivityPageReq req) {
        LambdaQueryWrapper<NftActivity> queryWrap = new QueryWrapper<NftActivity>().lambda()
                .eq(NftActivity::getMintAddress, req.getMintAddress())
                .orderByDesc(NftActivity::getCreateTime);
        Page<NftActivity> page = page(new Page<>(req.getCurrent(), req.getSize()), queryWrap);
        List<NftActivity> records = page.getRecords();
        if (CollectionUtils.isEmpty(records)) {
            return page.convert(p -> null);
        }
        return page.convert(p -> {
            NftActivityResp resp = new NftActivityResp();
            BeanUtils.copyProperties(p, resp);
            resp.setDate(RelativeDateFormat.convert(new Date(p.getCreateTime()), new Date()));
            resp.setDateFormat(p.getCreateTime().toString());
            // mint事件to为发起人地址
            if (NftActivityEnum.MINT.getCode() == Integer.parseInt(p.getActivityType())) {
                resp.setToAddress(p.getFromAddress());
                resp.setFromAddress(null);
            }
            return resp;
        });
    }

    @Override
    public Long queryLastUpdateTime(String mintAddress) {
        LambdaQueryWrapper<NftActivity> queryWrap = new QueryWrapper<NftActivity>().lambda()
                .eq(NftActivity::getMintAddress, mintAddress)
                .orderByDesc(NftActivity::getCreateTime)
                .last("limit 1");
        NftActivity nftActivity = getOne(queryWrap);
        if (nftActivity == null) {
            return null;
        }
        return nftActivity.getCreateTime();
    }
}
