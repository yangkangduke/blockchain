package com.seeds.admin.dto.merchant.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;


/**
 * 系统商家
 * 
 * @author hang.yu
 * @date 2022/7/19
 */
@Data
@ApiModel(value = "系统商家信息")
public class SysMerchantResp {

	@ApiModelProperty(value = "id")
	private Long id;

	@ApiModelProperty(value = "商家名称")
	private String name;

	@ApiModelProperty(value = "负责人id")
	private Long leaderId;

	@ApiModelProperty(value = "负责人姓名")
	private String leaderName;

	@ApiModelProperty(value = "联系方式")
	private String mobile;

	@ApiModelProperty(value = "商家状态  0：停用   1：正常")
	private Integer status;

	@ApiModelProperty(value = "网站地址")
	private String url;

	@ApiModelProperty(value = "用户列表")
	private List<SysMerchantUserResp> users;

}