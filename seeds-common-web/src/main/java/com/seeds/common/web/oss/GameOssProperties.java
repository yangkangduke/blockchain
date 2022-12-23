
package com.seeds.common.web.oss;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;


@Data
@ConfigurationProperties(prefix = "game")
public class GameOssProperties {
	private GameOss1Properties oss1;
	private GameOss2Properties oss2;
	private GameOss3Properties oss3;
}
