package com.seeds.admin.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;


/**
 * 系统合约上链记录
 * 
 * @author hang.yu
 * @date 2022/9/15
 */
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("sys_transfer_record")
public class SysTransferRecordEntity extends BaseEntity {

	/**
	 * 块编号
	 */
	@TableField("block_num")
	private Long blockNum;

	/**
	 * 交易哈希
	 */
	@TableField("trans_hash")
	private String transHash;

	/**
	 * 来自地址
	 */
	@TableField("from_address")
	private String fromAddress;

	/**
	 * 去往地址
	 */
	@TableField("to_address")
	private String toAddress;

	/**
	 * 价值
	 */
	@TableField("value")
	private BigDecimal value;

	/**
	 * 时间戳
	 */
	@TableField("timestamp")
	private String timestamp;

	/**
	 * 删除标记  0：未删除
	 */
	@TableLogic(value = "0", delval = "NULL")
	@TableField("delete_flag")
	private Integer deleteFlag;

}