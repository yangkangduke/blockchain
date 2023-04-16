package com.seeds.admin.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Map;

/**
 * @author: hewei
 * @date 2023/4/16
 */
@Data
public class SkinNftPushAutoIdReq {

    @NotNull
    private Map<Long, Long> autoIds;
}
