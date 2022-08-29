package com.seeds.uc.dto.redis;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * @author allen
 * @email allen.hua.ai@gmail.com
 * @date 2020/8/27
 */
@Data
@Builder
public class GenGoogleAuth implements Serializable {
    private String secret;
    private Long expireAt;
}
