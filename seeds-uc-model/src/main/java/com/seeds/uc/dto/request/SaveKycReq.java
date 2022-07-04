package com.seeds.uc.dto.request;

import lombok.Data;

/**
 * @author allen
 * @email allen.hua.ai@gmail.com
 * @date 2020/8/25
 */
@Data
public class SaveKycReq {
    private String countryCode;
    private String firstName;
    private String lastName;
    private String identity;
    private String identityFileUuid;
}
