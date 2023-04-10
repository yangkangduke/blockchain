package com.seeds.game.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: hewei
 * @date 2023/4/5
 */
@TableName("ga_update_backpack_error_log")
@ApiModel(value = "ga_update_backpack_error_log", description = "更新背包数据失败记录")
@Data
public class UpdateBackpackErrorLog {
    private Long id;

    @ApiModelProperty("nft事件id")
    private Long eventId;

    @ApiModelProperty("nft address")
    private String mintAddress;

    @ApiModelProperty("isAutoDeposit")
    private Integer isAutoDeposit;

    @ApiModelProperty("创建时间")
    private Long createdAt;
}
