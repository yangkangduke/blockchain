package com.seeds.admin.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author hang.yu
 * @date 2022/7/21
 */
@Data
@ApiModel(value = "SysGamePageReq", description = "游戏分页请求入参")
public class SysGamePageReq extends PageReq {

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "游戏类别id")
    private Long typeId;

    @ApiModelProperty(value = "排序类型 1.Newest 2.Rank 3.Price")
    private Integer sortType;

    @ApiModelProperty(value = "倒叙排序 0.正序 1.倒序")
    private Integer descFlag;

    @JsonIgnore
    private Integer status;

}
