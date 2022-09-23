package com.seeds.notification.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NoticeDTO implements Serializable {
	/**
	 * 用户id
	 */
	@ApiModelProperty("用户id")
	private List<Long> ucUserIds;

	/**
	 * 消息内容
	 */
	@ApiModelProperty("消息内容")
	private String content;

	@ApiModelProperty("推送时间")
	private Long pushTime;

}
