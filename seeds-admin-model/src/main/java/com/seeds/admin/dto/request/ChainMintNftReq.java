package com.seeds.admin.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class ChainMintNftReq {
    private String name;
    private String description;
    private List<ChainNftAttributes> attributes;
}
