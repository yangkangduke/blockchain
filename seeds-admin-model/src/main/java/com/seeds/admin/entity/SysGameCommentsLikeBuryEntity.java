package com.seeds.admin.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 游戏评论点赞、踩
 *
 * @TableName sys_game_comments_like_bury
 */

@TableName(value = "sys_game_comments_like_bury")
@Data
public class SysGameCommentsLikeBuryEntity extends BaseEntity {
    /**
     * 评论id
     */
    private Long commentsId;

    /**
     * UC端用户id
     */
    private Long ucUserId;
    /**
     * type  1 点赞 2 踩
     */
    private Integer type;

}