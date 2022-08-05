package com.seeds.uc.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
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
@TableName("uc_file")
@ApiModel(value = "UcFile对象", description = "文件管理表")
@Data
@Builder
public class UcFile implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("编号")
    @TableId(value = "id", type = IdType.AUTO)
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

    @ApiModelProperty("创建人")
    private Long createdBy;

    @ApiModelProperty("修改人")
    private Long updatedBy;

    @ApiModelProperty("上传时间")
    private Long createdAt;

    @ApiModelProperty("更新时间")
    private Long updatedAt;

    @ApiModelProperty("删除标记  1：已删除   0：未删除")
    private Boolean deleteFlag;

}
