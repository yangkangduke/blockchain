package com.seeds.account.mapper;

import com.seeds.account.enums.ChainCommonStatus;
import com.seeds.account.enums.ChainTxnReplaceAppType;
import com.seeds.account.model.ChainTxnReplace;
import com.seeds.common.enums.Chain;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ChainTxnReplaceMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_chain_transaction_replacement
     *
     * @mbg.generated Wed Jan 13 15:04:17 CST 2021
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_chain_transaction_replacement
     *
     * @mbg.generated Wed Jan 13 15:04:17 CST 2021
     */
    int insert(ChainTxnReplace record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_chain_transaction_replacement
     *
     * @mbg.generated Wed Jan 13 15:04:17 CST 2021
     */
    ChainTxnReplace selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_chain_transaction_replacement
     *
     * @mbg.generated Wed Jan 13 15:04:17 CST 2021
     */
    List<ChainTxnReplace> selectAll();

    int updateByPrimaryKey(ChainTxnReplace record);

    /**
     * 更新状态
     *
     * @param id
     * @param status
     * @return
     */
    int updateStatusByPrimaryKey(Long id, ChainCommonStatus status);

    List<ChainTxnReplace> getListByStatusAndType(@Param("statusList") List<Integer> statusList,
                                                 @Param("type") ChainTxnReplaceAppType type);

    List<ChainTxnReplace> getListByChainStatusAndType(@Param("chain") Chain chain,
                                                      @Param("status") ChainCommonStatus status,
                                                      @Param("type") ChainTxnReplaceAppType type);

    /**
     * 根据状态来查询
     *
     * @param status
     * @return
     */
    List<ChainTxnReplace> getListByStatus(Integer status);

    List<ChainTxnReplace> getListByStatusList(@Param("statusList") List<Integer> statusList);

    List<ChainTxnReplace> selectByAppIdAndAppType(@Param("appId") Long appId,
                                                  @Param("type") ChainTxnReplaceAppType type);
}