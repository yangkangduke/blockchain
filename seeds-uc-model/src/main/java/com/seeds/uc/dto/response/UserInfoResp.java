package com.seeds.uc.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @author yk
 * @email 819628513@qq.com
 * @date 2022/07/13
 */
@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserInfoResp {

    @ApiModelProperty("Primary Key")
    private Long id;
    @ApiModelProperty(value = "邮箱")
    private String email;
    @ApiModelProperty(value = "昵称")
    private String nickname;
    @ApiModelProperty(value = "头像")
    private String avatar;
    @ApiModelProperty(value = "头像url")
    private String avatarUrl;
    @ApiModelProperty(value = "matemask地址")
    private String publicAddress;
    @ApiModelProperty(value = "创建时间（时间戳）")
    private String createdAt;
    @ApiModelProperty(value = "策略")
    private List<UcSecurityStrategyResp> securityStrategyList;

}