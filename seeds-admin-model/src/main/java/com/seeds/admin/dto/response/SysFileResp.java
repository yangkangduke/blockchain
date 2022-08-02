package com.seeds.admin.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * 系统文件信息
 * 
 * @author hang.yu
 * @date 2022/8/02
 */
@Data
@ApiModel(value = "SysFileResp", description = "系统文件信息")
public class SysFileResp {

	@ApiModelProperty(value = "文件id")
	private Long fileId;

	@ApiModelProperty(value = "对象名")
	private String objectName;

}