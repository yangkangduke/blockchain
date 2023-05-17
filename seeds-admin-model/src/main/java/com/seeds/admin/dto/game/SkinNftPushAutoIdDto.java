package com.seeds.admin.dto.game;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: hewei
 * @date 2023/4/16
 */
@Data
public class SkinNftPushAutoIdDto {
    private Map<Long, Long> autoIds = new HashMap<>();
}
