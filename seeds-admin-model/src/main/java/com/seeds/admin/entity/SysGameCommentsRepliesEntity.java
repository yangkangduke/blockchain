package com.seeds.admin.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 游戏评论回复
 *
 * @TableName sys_game_comments_replies
 */

@TableName(value = "sys_game_comments_replies")
@Data
public class SysGameCommentsRepliesEntity {
    private Long id;
    /**
     * 评论id
     */
    private Long commentsId;
    /**
     * uc端用户ID
     */
    private Long ucUserId;
    /**
     * uc端用户名字
     */
    private String ucUserName;
    /**
     * 回复时间
     */
    private Long replyTime;
    /**
     * 回复内容
     */
    private String replies;
}