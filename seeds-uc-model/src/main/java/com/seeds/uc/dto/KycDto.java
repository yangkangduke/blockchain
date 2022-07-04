package com.seeds.uc.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class KycDto {
    private long id;
    private long uid;
    private String countryCode;
    private String firstName;
    private String lastName;
    private String identity;
    private long createdAt;
    private long updatedAt;

    private int status;
    private String comment;
}