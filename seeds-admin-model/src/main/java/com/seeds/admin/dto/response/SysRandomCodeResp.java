package com.seeds.admin.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * 随机码
 * 
 * @author hang.yu
 * @date 2022/11/08
 */
@Data
@ApiModel(value = "SysRandomCodeResp", description = "随机码")
public class SysRandomCodeResp {

	@ApiModelProperty(value = "id")
	private Long id;

	@ApiModelProperty(value = "批次号")
	private String batchNo;

	@ApiModelProperty(value = "类型 1：邀请码")
	private Integer type;

	@ApiModelProperty(value = "长度")
	private Integer length;

	@ApiModelProperty(value = "数量")
	private Integer number;

	@ApiModelProperty(value = "状态  0：正常  1：生成中  2：生成失败  3：导出中  6：导出失败")
	private Integer status;

	@ApiModelProperty(value = "表格地址")
	private String excelUrl;

	@ApiModelProperty(value = "描述")
	private String desc;

	@ApiModelProperty(value = "过期时间")
	private Long expireTime;

	@ApiModelProperty(value = "创建时间")
	private Long createdAt;

}