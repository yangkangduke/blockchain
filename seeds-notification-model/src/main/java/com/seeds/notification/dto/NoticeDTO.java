package com.seeds.notification.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NoticeDTO implements Serializable {
	/**
	 * 用户id
	 */
	@ApiModelProperty("用户id")
	private Long ucUserId;

	/**
	 * 消息内容
	 */
	@ApiModelProperty("消息内容")
	private String content;

}
