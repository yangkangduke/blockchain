package com.seeds.uc.model;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.seeds.uc.enums.ClientStateEnum;
import com.seeds.uc.enums.ClientTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * user table
 * </p>
 *
 * @author yk
 * @since 2022-07-14
 */
@TableName("uc_user")
@ApiModel(value = "UcUser对象", description = "user table")
@Data
@Builder
public class UcUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("Primary Key")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("user password")
    private String password;

    @ApiModelProperty("用户状态， 0-无效状态，1-正常，2-冻结，3-注销")
    private ClientStateEnum state;

    @ApiModelProperty("用户类型， 0-无效类型，1-普通用户")
    private ClientTypeEnum type;

    @ApiModelProperty("国家代码(手机用)")
    private String countryCode;

    @ApiModelProperty("国籍(必选)")
    private String nationality;

    @ApiModelProperty("手机号, 全世界不会一样")
    private String phone;

    @ApiModelProperty("邮箱")
    private String email;

    @ApiModelProperty("ga 密钥")
    private String gaSecret;

    @ApiModelProperty("盐")
    private String salt;

    @ApiModelProperty("update time")
    private Long updatedAt;

    @ApiModelProperty("create time")
    private Long createdAt;

    @ApiModelProperty("登陆账号")
    private String account;

    @ApiModelProperty("昵称")
    private String nickname;
    @ApiModelProperty("钱包地址，metamask")
    private String publicAddress;
    @ApiModelProperty("随机数，metamask登陆时使用")
    private String nonce;


}
