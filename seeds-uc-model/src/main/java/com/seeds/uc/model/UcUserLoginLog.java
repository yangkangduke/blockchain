package com.seeds.uc.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
import com.seeds.uc.enums.AccountTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * @author: hewei
 * @date 2022/9/28
 */
@TableName("uc_user_login_log")
@ApiModel(value = "UcUserLoginLog对象", description = "uc_user_login_log table")
@Data
public class UcUserLoginLog implements Serializable {

    @ApiModelProperty("primary key")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("用户id")
    private Long ucUserId;

    @ApiModelProperty("用户邮箱")
    private String email;

    @ApiModelProperty("登录ip")
    private String loginIp;

    @ApiModelProperty("登陆时间")
    private Long loginTime;
}
