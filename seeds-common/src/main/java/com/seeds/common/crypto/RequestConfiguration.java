package com.seeds.common.crypto;

import lombok.Builder;
import lombok.Getter;

/**
 * @author rickierao
 * @email antilaw@yahoo.com
 * @date 2021/1/24
 */

@Getter
@Builder
/**
 * The auth definition that client and server should comply
 */
public class RequestConfiguration {
    public static final String DEFAULT_SIGNATURE_HTTP_HEADER = "X-Auth-Signature";
    public static final String DEFAULT_TIMESTAMP_HTTP_HEADER = "X-Auth-Timestamp";
    public static final String DEFAULT_VERSION_HTTP_HEADER = "X-Auth-Version";
    public static final String DEFAULT_ACCESS_KEY_HTTP_HEADER = "X-Auth-Access-Key";
    public static final Version DEFAULT_VERSION = Version.V3;

    private final String signatureHttpHeader;
    private final String timestampHttpHeader;
    private final String versionHttpHeader;
    private final String accessKeyHttpHeader;
    private final Version version;

    public RequestConfiguration() {
        this(DEFAULT_SIGNATURE_HTTP_HEADER,
                DEFAULT_TIMESTAMP_HTTP_HEADER,
                DEFAULT_VERSION_HTTP_HEADER,
                DEFAULT_ACCESS_KEY_HTTP_HEADER,
                DEFAULT_VERSION);
    }

    public RequestConfiguration(Version v) {
        this(DEFAULT_SIGNATURE_HTTP_HEADER,
                DEFAULT_TIMESTAMP_HTTP_HEADER,
                DEFAULT_VERSION_HTTP_HEADER,
                DEFAULT_ACCESS_KEY_HTTP_HEADER,
                v);
    }

    public RequestConfiguration(String signatureHttpHeader,
                                String timestampHttpHeader,
                                String versionHttpHeader,
                                String accessKeyHttpHeader,
                                Version version) {
        this.signatureHttpHeader = signatureHttpHeader;
        this.timestampHttpHeader = timestampHttpHeader;
        this.versionHttpHeader = versionHttpHeader;
        this.accessKeyHttpHeader = accessKeyHttpHeader;
        this.version = version;
    }
}
