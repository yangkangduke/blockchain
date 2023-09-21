package com.seeds.notification.dto;

import com.seeds.notification.enums.NoticeTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationDto implements Serializable {
	/**
	 * Sender who send the notification, generally is the business service name.
	 */
	@ApiModelProperty("消息发送者")
	private String sender;

	/**
	 * The receiver of the notification, generally is userId.
	 */
	@ApiModelProperty("消息接受者")
	private List<Long> receivers;

	/**
	 * @see NoticeTypeEnum
	 */
	@ApiModelProperty("通知类型")
	private Integer notificationType;

	@ApiModelProperty("消息内容")
	private Map<String, Object> values = new HashMap<>();
}