package com.seeds.common.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * @author allen
 * @email allen.hua.ai@gmail.com
 * @date 2020/8/1
 */
@Data
@Builder
public class LoginDTO implements Serializable {
    private Long userId;
    private String loginName;
    private Long expireAt;
}
