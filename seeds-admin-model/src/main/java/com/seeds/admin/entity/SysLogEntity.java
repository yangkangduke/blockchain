package com.seeds.admin.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author: hewei
 * @date 2022/7/26
 */

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("sys_log")
public class SysLogEntity {

    @TableId(value="id", type = IdType.AUTO)
    private Long id;

    /**
     * 操作人
     */
    private Long operator;

    /**
     * 操作人名字
     */
    private String operatorName;

    /**
     * 具体操作
     */
    private String operation;

    /**
     * 执行的方法
     */
    private String method;

    /**
     * 请求参数
     */
    private String params;

    /**
     * 真实IP地址
     */
    private String ip;

    @TableField(fill = FieldFill.INSERT)
    private Long createdAt;
}
