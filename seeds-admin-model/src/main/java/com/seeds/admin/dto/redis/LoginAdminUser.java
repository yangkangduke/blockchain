package com.seeds.admin.dto.redis;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * @author hang.yu
 * @date 2022/7/13
 */
@Data
@Builder
public class LoginAdminUser implements Serializable {

    private Long userId;

    private Long expireAt;

}
