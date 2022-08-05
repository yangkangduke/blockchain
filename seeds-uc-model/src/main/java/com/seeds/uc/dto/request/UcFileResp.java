package com.seeds.uc.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;


/**
 * 系统文件信息
 * 
 * @author yk
 * @date 2022/08/05
 */
@Data
@Builder
public class UcFileResp implements Serializable {

	@ApiModelProperty(value = "文件id")
	private Long fileId;
	@ApiModelProperty(value = "对象名")
	private String objectName;
	@ApiModelProperty(value = "桶")
	private String bucketName;
	@ApiModelProperty(value = "url")
	private String url;

}