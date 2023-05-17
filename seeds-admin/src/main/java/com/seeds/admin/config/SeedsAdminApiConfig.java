package com.seeds.admin.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @author hewei
 * @date 2023/4/26
 */
@Data
@Configuration
public class SeedsAdminApiConfig {

    @Value("${seeds.api.baseDomain:http://192.168.6.100:8002}")
    private String baseDomain;

    @Value("${seeds.api.admin.mintNft:/api/admin/mintNft}")
    private String mintNft;

    @Value("${seeds.api.admin.cancelAsset:/api/admin/cancelAssetV2}")
    private String cancelAsset;

    @Value("${seeds.api.admin.cancelAuction:/api/admin/cancelAuction}")
    private String cancelAuction;

    @Value("${seeds.api.admin.english:/api/admin/englishV2}")
    private String english;

    @Value("${seeds.api.admin.listAsset:/api/admin/listAsset}")
    private String listAsset;

    @Value("${seeds.api.admin.withdrawNft:/api/admin/withdrawNft}")
    private String withdrawNft;

    @Value("${seeds.api.admin.shadowUrl:https://shdw-drive.genesysgo.net/EbV3SUa9vvvB2UmyrHpXAtwXAQHeFLEGesWJKqwmAbT7/}")
    private String shadowUrl;

    @Value("${seeds.api.admin.uploadToShadow:http://192.168.6.100:8005/shdw/editFile}")
    private String uploadToShadow;
}
