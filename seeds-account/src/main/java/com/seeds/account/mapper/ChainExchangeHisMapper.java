package com.seeds.account.mapper;

import com.seeds.account.enums.ChainCommonStatus;
import com.seeds.account.enums.ChainExchangeAction;
import com.seeds.account.model.ChainExchangeHis;
import com.seeds.common.enums.Chain;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ChainExchangeHisMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_chain_exchange_his
     *
     * @mbg.generated Wed Jan 06 22:13:30 CST 2021
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_chain_exchange_his
     *
     * @mbg.generated Wed Jan 06 22:13:30 CST 2021
     */
    int insert(ChainExchangeHis record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_chain_exchange_his
     *
     * @mbg.generated Wed Jan 06 22:13:30 CST 2021
     */
    ChainExchangeHis selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_chain_exchange_his
     *
     * @mbg.generated Wed Jan 06 22:13:30 CST 2021
     */
    List<ChainExchangeHis> selectAll();

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_chain_exchange_his
     *
     * @mbg.generated Wed Jan 06 22:13:30 CST 2021
     */
    int updateByPrimaryKey(ChainExchangeHis record);

    /**
     * 根据状态来查询
     *
     * @param action
     * @param statusList
     * @return
     */
    List<ChainExchangeHis> getListByStatus(ChainExchangeAction action, List<ChainCommonStatus> statusList);

    /**
     * 更新状态
     *
     * @param id
     * @param status
     * @return
     */
    int updateStatusByPrimaryKey(Long id, ChainCommonStatus status);

    ChainExchangeHis getByChainHash(@Param("chain") Chain chain, @Param("txHash") String txHash);

    List<ChainExchangeHis> selectByChainStatusAndTimestamp(@Param("chain") Chain chain,
                                                           @Param("status") ChainCommonStatus status,
                                                           @Param("timestamp") Long timestamp);

    List<ChainExchangeHis> selectByCurrencyActionAndStatus(@Param("sourceCurrency") String sourceCurrency,
                                                           @Param("targetCurrency") String targetCurrency,
                                                           @Param("action") ChainExchangeAction action,
                                                           @Param("status") ChainCommonStatus status);

    List<ChainExchangeHis> selectByAction(@Param("action") ChainExchangeAction action);

    List<ChainExchangeHis> selectByChainActionStatusAndTimestamp(@Param("chain") Chain chain,
                                                                 @Param("action") ChainExchangeAction action,
                                                                 @Param("status") ChainCommonStatus status,
                                                                 @Param("timestamp") Long timestamp);

    List<ChainExchangeHis> selectByActionAndChain(@Param("action") ChainExchangeAction action,
                                                  @Param("chain") Chain chain);
}