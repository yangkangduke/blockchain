package com.seeds.account.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.seeds.account.model.ChainBlock;
import com.seeds.common.enums.Chain;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * Ethereum块跟踪 Mapper 接口
 * </p>
 *
 * @author yk
 * @since 2022-09-29
 */
public interface ChainBlockMapper extends BaseMapper<ChainBlock> {

    /**
     * 获取最后的Block(status=1)
     * @return
     */
    ChainBlock getLatestBlock(@Param("chain") Chain chain);

    /**
     * 回滚
     * @param chain
     * @param blockNumber
     * @param blockHash
     * @param status
     * @return
     */
    int rollbackBlock(Chain chain, long blockNumber, String blockHash, int status);
}
