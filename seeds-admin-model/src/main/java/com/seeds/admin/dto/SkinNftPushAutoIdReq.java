package com.seeds.admin.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.seeds.admin.jackson.CustomDeserializer;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: hewei
 * @date 2023/4/16
 */
@Data
public class SkinNftPushAutoIdReq {

    @NotNull
    @JsonDeserialize(using = CustomDeserializer.class)
    private Map<Long, Long> autoIds = new HashMap<>();
}
