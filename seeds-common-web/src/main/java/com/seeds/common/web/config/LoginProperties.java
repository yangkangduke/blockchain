
package com.seeds.common.web.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 邮件 配置信息
 *
 * @author yk
 * <p>
 * bucket 设置公共读权限
 */
@Data
@Component
@ConfigurationProperties(prefix = "login")
public class LoginProperties {

	private Boolean twofa;


}
