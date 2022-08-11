package com.seeds.admin.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ChainNftAttributes {
    @JsonProperty("trait_type")
    private String traitType;
    private String value;
}
