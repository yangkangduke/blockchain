
package com.seeds.common.web.config;

import com.seeds.common.web.oss.FileProperties;
import com.seeds.common.web.oss.OssAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;

/**
 * aws 自动配置类
 *
 * @author yk
 */
@Import({ LocalFileAutoConfiguration.class, OssAutoConfiguration.class })
@EnableConfigurationProperties({ FileProperties.class })
public class FileAutoConfiguration {

}
