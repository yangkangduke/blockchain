package com.seeds.uc.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.seeds.uc.enums.ClientAuthTypeEnum;
import com.seeds.uc.enums.ClientStateEnum;
import com.seeds.uc.enums.ClientTypeEnum;
import lombok.Data;

/**
* @author yk
 * @date 2020/7/31
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDto {
    private Long uid;
    private ClientStateEnum state;
    private ClientTypeEnum type;
    private ClientAuthTypeEnum authType;
    private String countryCode;
    private String nationality;
    private String phone;
    private String email;
    private Long updatedAt;
    private Long createdAt;
}
