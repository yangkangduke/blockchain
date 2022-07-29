package com.seeds.uc.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.seeds.uc.enums.ClientAuthTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 安全策略表
 * </p>
 *
 * @author yk
 * @since 2022-07-15
 */
@TableName("uc_security_strategy")
@ApiModel(value = "UcSecurityStrategy对象", description = "安全策略表")
@Data
@Builder
public class UcSecurityStrategy implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("用户的id")
    private Long uid;

    @ApiModelProperty("是否开启了验证")
    private Boolean needAuth;

    @ApiModelProperty("验证的类型ClientAuthTypeEnum 1-matemask, 2-email, 3-ga")
    private ClientAuthTypeEnum authType;

    @ApiModelProperty("创建时间")
    private Long createdAt;

    @ApiModelProperty("操作时间")
    private Long updatedAt;

}
