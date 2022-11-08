package com.seeds.admin.dto.response;

import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * 随机码导出明细
 *
 * @author hang.yu
 * @date 2022/11/08
 */
@Data
@ApiModel(value = "SysRandomCodeExportResp", description = "随机码导出明细")
public class SysRandomCodeExportResp {

	@ApiModelProperty(value = "随机码")
	@ExcelProperty(value = "随机码")
	private String code;

	@ApiModelProperty(value = "使用标记 0：未使用 1：已使用")
	@ExcelProperty(value = "是否使用")
	private String useFlag;

	@ApiModelProperty(value = "描述")
	@ExcelProperty(value = "描述")
	private String desc;

	@ApiModelProperty(value = "过期时间")
	@ExcelProperty(value = "过期时间")
	private String expireTime;

	@ApiModelProperty(value = "创建时间")
	@ExcelProperty(value = "生成时间")
	private String createdAt;

}