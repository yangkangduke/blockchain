package com.seeds.uc.model.cache.dto;

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
public class LoginUser implements Serializable {
    private Long userId;
    private String loginName;
    private Long expireAt;
}
