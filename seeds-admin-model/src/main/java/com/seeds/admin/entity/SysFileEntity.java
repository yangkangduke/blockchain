package com.seeds.admin.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 系统文件
 * 
 * @author hang.yu
 * @date 2022/8/02
 */
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("sys_file")
public class SysFileEntity extends BaseEntity {

	/**
	 * 对象名
	 */
	@TableField("object_name")
	private String objectName;

	/**
	 * 桶名
	 */
	@TableField("bucket_name")
	private String bucketName;

	/**
	 * 文件名
	 */
	@TableField("file_name")
	private String fileName;

	/**
	 * 类型
	 */
	@TableField("type")
	private String type;

	/**
	 * 类型
	 */
	@TableField("file_size")
	private Long fileSize;

	/**
	 * 删除标记  1：已删除   0：未删除
	 */
	@TableField("delete_flag")
	@TableLogic
	private Integer deleteFlag;

}