

package com.seeds.common.file.core;

import com.seeds.common.file.local.LocalFileProperties;
import com.seeds.common.file.oss.OssProperties;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 文件 配置信息
 *
 * @author lengleng
 * <p>
 * bucket 设置公共读权限
 */
@Data
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
