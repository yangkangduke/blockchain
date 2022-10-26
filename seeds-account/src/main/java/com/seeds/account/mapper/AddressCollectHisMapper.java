package com.seeds.account.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.seeds.account.enums.ChainCommonStatus;
import com.seeds.account.model.AddressCollectHis;
import com.seeds.common.enums.Chain;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 处理地址归集
 *
 * @author yk
 */
@Mapper
public interface AddressCollectHisMapper {
    /**
     * 删除
     *
     * @param id
     * @return
     */
    int deleteByPrimaryKey(Long id);

    /**
     * 插入新的
     *
     * @param record
     * @return
     */
    int insert(AddressCollectHis record);

    /**
     * 更新
     *
     * @param record
     * @return
     */
    int updateByPrimaryKey(AddressCollectHis record);

    /**
     * @param id
     * @return
     */
    AddressCollectHis selectByPrimaryKey(Long id);

    /**
     * 获取所有
     *
     * @return
     */
    List<AddressCollectHis> selectAll();


    /**
     * 获取所有
     *
     * @return
     */
    List<AddressCollectHis> getByAddress(@Param("chain") int chain, @Param("startTime") long startTime, @Param("endTime") long endTime,
                                         @Param("fromAddress") String fromAddress, @Param("toAddress") String toAddress);

    /**
     * @param txHash
     * @return
     */
    AddressCollectHis getByChainHash(@Param("chain") Chain chain, @Param("txHash") String txHash);

    /**
     * @param status
     * @return
     */
    List<AddressCollectHis> getByStatus(@Param("status") int status);

    /**
     * @param orderId
     * @return
     */
    List<AddressCollectHis> getByOrderId(@Param("orderId") long orderId);

    IPage<AddressCollectHis> getByChainStatusOrderTypeAndTimestamp(Page page,
                                                                  @Param("chain") Chain chain,
                                                                  @Param("status") int status,
                                                                  @Param("type") int type,
                                                                  @Param("timestamp") long timestamp);

    IPage<AddressCollectHis> getHotWalletByChainStatusAndTimestamp(Page page,
                                                                   @Param("chain") Chain chain,
                                                                   @Param("status") int status,
                                                                   @Param("timestamp") long timestamp);

    /**
     * 更新状态
     *
     * @param id
     * @param status
     * @return
     */
    int updateStatusByPrimaryKey(Long id, ChainCommonStatus status);

}