
package com.seeds.common.web.oss;

import com.seeds.common.web.config.LocalFileProperties;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 文件 配置信息
 *
 * @author yk
 * <p>
 * bucket 设置公共读权限
 */
@Data
@Component
@ConfigurationProperties(prefix = "file")
public class FileProperties {

	/**
	 * 默认的存储桶名称
	 */
	private String bucketName = "local";

	/**
	 * 本地文件配置信息
	 */
	private LocalFileProperties local;

	/**
	 * oss 文件配置信息
	 */
	private OssProperties oss;

}
