
package com.seeds.common.web.oss;


import com.seeds.common.web.oss.http.OssEndpoint;
import com.seeds.common.web.oss.service.OssTemplate;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
/**
 * aws 自动配置类
 *
 * @author yk
 * @author 858695266
 */
@AllArgsConstructor
public class OssAutoConfiguration {

	private final FileProperties properties;

	@Bean
	@Primary
	@ConditionalOnMissingBean(OssTemplate.class)
	@ConditionalOnProperty(name = "file.oss.enable", havingValue = "true")
	public FileTemplate ossTemplate() {
		return new OssTemplate(properties);
	}

	@Bean
	@ConditionalOnProperty(name = "file.oss.info", havingValue = "true")
	public OssEndpoint ossEndpoint(OssTemplate template) {
		return new OssEndpoint(template);
	}

}
