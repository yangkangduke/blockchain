package com.seeds.admin.dto.request;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ChainMintNftReq {
    private Long id;
    private String name;
    private String description;
    private List<ChainNftAttributes> attributes;
}
