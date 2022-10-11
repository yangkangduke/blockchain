package com.seeds.admin.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * NFT触发事件
 * 
 * @author hang.yu
 * @date 2022/10/8
 */
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("sys_nft_event_record")
public class SysNftEventRecordEntity extends BaseEntity {

	/**
	 * 事件的id
	 */
	@TableField("event_id")
	private Long eventId;

	/**
	 * NFT的id
	 */
	@TableField("nft_id")
	private Long nftId;

	/**
	 * 触发时间
	 */
	@TableField("trigger_time")
	private Long triggerTime;

}