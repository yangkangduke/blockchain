package com.seeds.uc.dto.redis;

import com.seeds.uc.enums.AuthCodeUseTypeEnum;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * @author allen
 * @email allen.hua.ai@gmail.com
 * @date 2020/8/25
 */
@Data
@Builder
public class SecurityAuth implements Serializable {
    private Long uid;
    private AuthCodeUseTypeEnum useType;
    private Long expireAt;
}
