
package com.seeds.uc.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 重置密码
 *
 * @author yk
 * <p>
 * bucket 设置公共读权限
 */
@Data
@Component
@ConfigurationProperties(prefix = "resetpassword")
public class ResetPasswordProperties {

	private String url;

}
