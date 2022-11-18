package com.seeds.uc.dto.request;

import com.seeds.common.dto.PageReq;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
* @author yk
 * @date 2020/7/31
 */
@Data
public class AllUserReq extends PageReq {
    @ApiModelProperty(value = "昵称")
    private String nickname;
    @ApiModelProperty(value = "邮箱")
    private String email;
    @ApiModelProperty(value = "用户id")
    private Long id;
    @ApiModelProperty(value = "用户状态， 0-无效状态，1-正常，2-冻结，3-注销")
    private Integer state;
    @ApiModelProperty(value = "绑定的钱包地址")
    private String publicAddress;

}
