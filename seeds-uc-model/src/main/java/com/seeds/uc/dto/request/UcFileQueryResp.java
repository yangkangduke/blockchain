package com.seeds.uc.dto.request;

import com.seeds.common.dto.PageReq;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

/**
 * <p>
 * 文件管理表
 * </p>
 *
 * @author yk
 * @since 2022-08-05
 */
@Data
@Builder
public class UcFileQueryResp extends PageReq {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("编号")
    private Long id;

    @ApiModelProperty("对象名")
    private String objectName;

    @ApiModelProperty("桶名")
    private String bucketName;

    @ApiModelProperty("文件名")
    private String fileName;

    @ApiModelProperty("类型")
    private String type;

    @ApiModelProperty("文件大小")
    private Long fileSize;

}
