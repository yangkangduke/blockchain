package com.seeds.game.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seeds.game.entity.NftFeeOpLog;
import com.seeds.game.mapper.NftFeeOpLogMapper;
import com.seeds.game.service.INftFeeOpLogService;
import org.springframework.stereotype.Service;


/**
 * <p>
 * nft手续费记录
 * </p>
 *
 * @author hang.yu
 * @since 2023-05-09
 */
@Service
public class NftFeeOpLogServiceImpl extends ServiceImpl<NftFeeOpLogMapper, NftFeeOpLog> implements INftFeeOpLogService {

    @Override
    public NftFeeOpLog queryByTxHash(String txHash) {
        LambdaQueryWrapper<NftFeeOpLog> queryWrap = new QueryWrapper<NftFeeOpLog>().lambda()
                .eq(NftFeeOpLog::getTxHash, txHash);
        return getOne(queryWrap);
    }
}
