package com.seeds.admin.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 游戏评论表
 *
 * @TableName sys_game_comments
 */

@TableName(value = "sys_game_comments")
@Data
public class SysGameCommentsEntity extends BaseEntity {

    /**
     * 游戏id
     */
    private Long gameId;

    /**
     * 游戏名称
     */
    private String gameName;

    /**
     * uc端用户id
     */
    private Long ucUserId;

    /**
     * uc端用户名字
     */
    private String ucUserName;

    /**
     * 评价时间
     */
    private Long commentsTime;

    /**
     * 用户评价
     */
    private String comments;

    /**
     * 评分
     */
    private BigDecimal star;
    /**
     * 点赞数/有用数量
     */
    private Integer likes;
    /**
     * 无用数量
     */
    private Integer bury;
    /**
     * 回复数量
     */
    private Integer replies;

    /**
     * 评价状态  0：无效   1：有效
     */
    private Integer status;
    /**
     * 删除标记  0：未删除  1：已删除
     */
    @TableField(value = "delete_flag")
    @TableLogic
    private Integer deleteFlag;

}