package com.seeds.account.model;

import com.seeds.account.enums.ChainCommonStatus;
import com.seeds.account.enums.ChainExchangeAction;
import com.seeds.common.enums.Chain;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChainExchangeHis {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_chain_exchange_his.f_id
     *
     * @mbg.generated Wed Jan 06 22:13:30 CST 2021
     */
    private Long id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_chain_exchange_his.f_create_time
     *
     * @mbg.generated Wed Jan 06 22:13:30 CST 2021
     */
    private Long createTime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_chain_exchange_his.f_update_time
     *
     * @mbg.generated Wed Jan 06 22:13:30 CST 2021
     */
    private Long updateTime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_chain_exchange_his.f_version
     *
     * @mbg.generated Wed Jan 06 22:13:30 CST 2021
     */
    private Long version;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_chain_exchange_his.f_user_id
     *
     * @mbg.generated Wed Jan 06 22:13:30 CST 2021
     */
    private Long userId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_chain_exchange_his.f_chain
     *
     * @mbg.generated Wed Jan 06 22:13:30 CST 2021
     */
    private Chain chain;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_exchange_his.f_source_currency
     *
     * @mbg.generated Mon Dec 21 21:08:59 CST 2020
     */
    private String sourceCurrency;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_exchange_his.f_source_amount
     *
     * @mbg.generated Mon Dec 21 21:08:59 CST 2020
     */
    private BigDecimal sourceAmount;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_exchange_his.f_target_currency
     *
     * @mbg.generated Mon Dec 21 21:08:59 CST 2020
     */
    private String targetCurrency;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_exchange_his.f_target_amount
     *
     * @mbg.generated Mon Dec 21 21:08:59 CST 2020
     */
    private BigDecimal targetAmount;
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_chain_exchange_his.f_from_address
     *
     * @mbg.generated Wed Jan 06 22:13:30 CST 2021
     */
    private String fromAddress;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_chain_exchange_his.f_to_address
     *
     * @mbg.generated Wed Jan 06 22:13:30 CST 2021
     */
    private String toAddress;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_chain_exchange_his.f_action
     *
     * @mbg.generated Wed Jan 06 22:13:30 CST 2021
     */
    private ChainExchangeAction action;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_chain_exchange_his.f_gas_price
     *
     * @mbg.generated Wed Jan 06 22:13:30 CST 2021
     */
    private Long gasPrice;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_chain_exchange_his.f_gas_limit
     *
     * @mbg.generated Wed Jan 06 22:13:30 CST 2021
     */
    private Long gasLimit;

    private Long gasUsed;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_chain_exchange_his.f_tx_fee
     *
     * @mbg.generated Wed Jan 06 22:13:30 CST 2021
     */
    private BigDecimal txFee;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_chain_exchange_his.f_block_number
     *
     * @mbg.generated Wed Jan 06 22:13:30 CST 2021
     */
    private Long blockNumber;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_chain_exchange_his.f_block_hash
     *
     * @mbg.generated Wed Jan 06 22:13:30 CST 2021
     */
    private String blockHash;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_chain_exchange_his.f_tx_hash
     *
     * @mbg.generated Wed Jan 06 22:13:30 CST 2021
     */
    private String txHash;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_chain_exchange_his.f_status
     *
     * @mbg.generated Wed Jan 06 22:13:30 CST 2021
     */
    private String nonce;

    private ChainCommonStatus status;

    private String txValue;

    private String txToAddress;
}