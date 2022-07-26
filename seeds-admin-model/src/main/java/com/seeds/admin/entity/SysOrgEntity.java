package com.seeds.admin.entity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 系统组织表
 * @TableName sys_org
 */
@Data
@TableName(value ="sys_org")
@EqualsAndHashCode(callSuper=false)
public class SysOrgEntity extends BaseEntity {

    /**
     * 组织ID
     */
    @TableField(value = "org_id")
    private Long orgId;

    /**
     * 父级组织ID
     */
    @TableField(value = "parent_org_id")
    private Long parentOrgId;

    /**
     * 组织名称
     */
    @TableField(value = "org_name")
    private String orgName;

    /**
     * 负责人
     */
    @TableField(value = "owner")
    private String owner;

    /**
     * 备注
     */
    @TableField(value = "comments")
    private String comments;

    /**
     * 删除标记  0：未删除  1：已删除
     */
    @TableField(value = "delete_flag")
    @TableLogic
    private Integer deleteFlag;

    /**
     * 排序
     */
    @TableField(value = "sort")
    private Integer sort;
}