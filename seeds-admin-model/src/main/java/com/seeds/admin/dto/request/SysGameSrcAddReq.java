package com.seeds.admin.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;


/**
 * @author: hewei
 * @date 2022/12/21
 */
@Data
@ApiModel(value = "SysGameSrcAddReq", description = "游戏资源新增")
public class SysGameSrcAddReq {

	@ApiModelProperty(value = "资源类型 1 主页视频；2 安装包；3 补丁")
	@NotNull(message = "srcType name cannot be empty")
	private Integer srcType;

	@ApiModelProperty(value = "操作系统 0 不限; 1 Windows; 2 mac")
	@NotNull(message = "os name cannot be empty")
	private Integer os;

	@ApiModelProperty(value = "备注")
	private String remark;

	@ApiModelProperty(value = "文件名")
	private String fileName;

	@ApiModelProperty(value = "文件大小")
	private Long size;
}
