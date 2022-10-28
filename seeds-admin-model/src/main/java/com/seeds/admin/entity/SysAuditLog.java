package com.seeds.admin.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

import com.seeds.admin.enums.Action;
import com.seeds.admin.enums.Module;
import com.seeds.admin.enums.SubModule;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>
 * sys audit log table
 * </p>
 *
 * @author yk
 * @since 2022-10-25
 */
@Builder
@TableName("sys_audit_log")
@ApiModel(value = "SysAuditLog对象", description = "sys audit log table")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SysAuditLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("Primary Key")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("模块名称")
    private Module module;

    @ApiModelProperty("子模块名称")
    private SubModule subModule;

    @ApiModelProperty("操作")
    private Action action;

    @ApiModelProperty("ip地址")
    private String ip;

    @ApiModelProperty("user id")
    private Long userId;

    @ApiModelProperty("user name")
    private String userName;

    @ApiModelProperty("修改后的数据")
    private String afterChange;

    @ApiModelProperty("数据主键")
    private String dataKey;

    @ApiModelProperty("update time")
    private Long updatedAt;

    @ApiModelProperty("create time")
    private Long createdAt;


}
