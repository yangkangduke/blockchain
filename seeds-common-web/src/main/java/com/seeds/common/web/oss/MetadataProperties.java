package com.seeds.common.web.oss;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author: hewei
 * @date 2023/4/23
 */

@Data
@ConfigurationProperties(prefix = "metadata")
public class MetadataProperties {
    private String bucketName;
    private String endpoint;

    /**
     * 区域
     */
    private String region;
}
