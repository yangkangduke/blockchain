package com.seeds.uc.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author yk
 * @email 819628513@qq.com
 * @date 2022/07/13
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UcUserResp {

    @ApiModelProperty("Primary Key")
    private Long id;
    @ApiModelProperty(value = "邮箱")
    private String email;
    @ApiModelProperty(value = "昵称")
    private String nickname;
    @ApiModelProperty(value = "matemask地址")
    private String publicAddress;
    @ApiModelProperty(value = "创建时间（时间戳）")
    private Long createdAt;
    @ApiModelProperty(value = "修改时间（时间戳）")
    private Long updatedAt;
    @ApiModelProperty(value = "用户状态， 0-无效状态，1-正常，2-冻结，3-注销")
    private Integer state;


}