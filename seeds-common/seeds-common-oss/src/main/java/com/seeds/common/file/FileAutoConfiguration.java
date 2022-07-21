
package com.seeds.common.file;

import com.seeds.common.file.core.FileProperties;
import com.seeds.common.file.local.LocalFileAutoConfiguration;
import com.seeds.common.file.oss.OssAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;

/**
 * aws 自动配置类
 *
 * @author lengleng
 * @author 858695266
 */
@Import({ LocalFileAutoConfiguration.class, OssAutoConfiguration.class })
@EnableConfigurationProperties({ FileProperties.class })
public class FileAutoConfiguration {

}
