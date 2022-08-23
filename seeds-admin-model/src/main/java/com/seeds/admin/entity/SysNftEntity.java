package com.seeds.admin.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;


/**
 * NFT
 * 
 * @author hang.yu
 * @date 2022/7/22
 */
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("sys_nft")
public class SysNftEntity extends BaseEntity {

	/**
	 * 编号
	 */
	@TableField("number")
	private String number;

	/**
	 * 图片链接
	 */
	@TableField("url")
	private String url;

	/**
	 * 描述
	 */
	@TableField("description")
	private String description;

	/**
	 * 名称
	 */
	@TableField("name")
	private String name;

	/**
	 * 游戏id
	 */
	@TableField("game_id")
	private Long gameId;

	/**
	 * 类别id
	 */
	@TableField("nft_type_id")
	private Long nftTypeId;

	/**
	 * 价格
	 */
	@TableField("price")
	private BigDecimal price;

	/**
	 * 单位 USDC
	 */
	@TableField("unit")
	private String unit;

	/**
	 * 是否在售  0：否   1：是
	 */
	@TableField("status")
	private Integer status;

	/**
	 * 状态  0：正常  1：创建中  2：创建失败  3：修改中  4：修改失败
	 */
	@TableField("init_status")
	private Integer initStatus;

	/**
	 * 错误信息
	 */
	@TableField("error_msg")
	private String errorMsg;

	/**
	 * 归属人id
	 */
	@TableField("owner_id")
	private String ownerId;

	/**
	 * 归属人名称
	 */
	@TableField("owner_name")
	private String ownerName;

	/**
	 * 合约地址
	 */
	@TableField("contract_address")
	private String contractAddress;

	/**
	 * 令牌id
	 */
	@TableField("token_id")
	private String tokenId;

	/**
	 * 令牌类型
	 */
	@TableField("token_standard")
	private String tokenStandard;

	/**
	 * 区块链
	 */
	@TableField("blockChain")
	private String blockChain;

	/**
	 * 元数据
	 */
	@TableField("metadata")
	private String metadata;

	/**
	 * 创建人报酬比例
	 */
	@TableField("creator_fees")
	private BigDecimal creatorFees;

	/**
	 * 删除标记  0：未删除
	 */
	@TableLogic(value = "0", delval = "NULL")
	@TableField("delete_flag")
	private Integer deleteFlag;
}