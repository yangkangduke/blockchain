package com.seeds.game.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author: hewei
 * @date 2023/3/6
 */
@TableName("ga_call_game_api_error_log")
@Data
public class CallGameApiErrorLogEntity {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    private String url;
    private String method;
    private String params;
    private String msg;
    // 重试次数
    private Integer retryNum;
    private Integer maxRetryNum;
    private Long callTime;
}
