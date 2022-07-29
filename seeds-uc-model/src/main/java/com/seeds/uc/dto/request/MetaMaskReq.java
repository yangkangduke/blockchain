package com.seeds.uc.dto.request;

import com.seeds.uc.enums.UserOperateEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author yk
 * @email 819628513@qq.com
 * @date 2022/07/13
 */
@Data
public class MetaMaskReq {

    @ApiModelProperty(value = "钱包地址", required = true)
    private String publicAddress;
    private String signature;
    @ApiModelProperty(value = "信息")
    private String message;
    @ApiModelProperty(value = "类型", required = true)
    private UserOperateEnum operateEnum;


}
