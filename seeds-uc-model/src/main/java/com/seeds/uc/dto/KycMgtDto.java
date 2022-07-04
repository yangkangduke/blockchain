package com.seeds.uc.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class KycMgtDto {
    private long id;
    private long uid;
    private String countryCode;
    private String firstName;
    private String lastName;
    private String identity;
    //private String identityFileUuid;
    private long createdAt;
    private long updatedAt;

    private int status;
    private String comment;
    private long operatorId;
    private String operatorName;
    private FileDto fileDto;
}