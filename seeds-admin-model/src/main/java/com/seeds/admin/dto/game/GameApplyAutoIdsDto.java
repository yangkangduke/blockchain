package com.seeds.admin.dto.game;

import lombok.Data;

import java.util.Set;

/**
 * @author: hewei
 * @date 2023/4/15
 */
@Data
public class GameApplyAutoIdsDto {
    private String optUser = "admin";
    private Set<Long> configIds;
}
