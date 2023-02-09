package com.seeds.admin.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;


/**
 * 游戏
 * 
 * @author hang.yu
 * @date 2022/7/21
 */
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("sys_game")
public class SysGameEntity extends BaseEntity {

	/**
	 * 游戏类别id
	 */
	@TableField("type_id")
	private Long typeId;

	/**
	 * 游戏名称
	 */
	@TableField("name")
	private String name;

	/**
	 * 简介
	 */
	@TableField("brief")
	private String brief;

	/**
	 * 价格
	 */
	@TableField("price")
	private BigDecimal price;

	/**
	 * 单位
	 */
	@TableField("unit")
	private String unit;

	/**
	 * 官方网址
	 */
	@TableField("official_url")
	private String officialUrl;

	/**
	 * 下载地址
	 */
	@TableField("download_url")
	private String downloadUrl;

	/**
	 * 社区地址
	 */
	@TableField("community_url")
	private String communityUrl;

	/**
	 * 开发者
	 */
	@TableField("developer")
	private String developer;

	/**
	 * 图片链接
	 */
	@TableField("picture_url")
	private String pictureUrl;

	/**
	 * 图片文件id
	 */
	@TableField("picture_file_id")
	private Long pictureFileId;

	/**
	 * 视频链接
	 */
	@TableField("video_url")
	private String videoUrl;

	/**
	 * 视频文件id
	 */
	@TableField("video_file_id")
	private Long videoFileId;

	/**
	 * 是否允许评论  0：不允许   1：允许
	 */
	@TableField("comments_allowed")
	private Integer commentsAllowed;

	/**
	 * 收藏量
	 */
	@TableField("collections")
	private Long collections;

	/**
	 * 介绍
	 */
	@TableField("introduction")
	private String introduction;

	/**
	 * 评分
	 */
	@TableField("`rank`")
	private BigDecimal rank;

	/**
	 * 访问键
	 */
	@TableField("access_key")
	private String accessKey;

	/**
	 * 密钥
	 */
	@TableField("secret_key")
	private String secretKey;

	/**
	 * 游戏状态  0：下架   1：正常
	 */
	@TableField("status")
	private Integer status;

	/**
	 * 游戏是否维护：0：维护中  1：正常
	 */
	@TableField("upkeep")
	private Integer upkeep;

	/**
	 * 删除标记  0：未删除
	 */
	@TableLogic(value = "0", delval = "NULL")
	@TableField("delete_flag")
	private Integer deleteFlag;

}