
package com.seeds.common.web.oss;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "oss3")
public class GameOss3Properties {

    private String bucketName;

    private String region;

    private String cdn;

}
