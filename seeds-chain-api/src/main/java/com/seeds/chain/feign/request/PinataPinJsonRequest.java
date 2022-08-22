package com.seeds.chain.feign.request;

import com.seeds.admin.dto.request.ChainNftAttributes;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PinataPinJsonRequest {
    private String name;
    private String description;
    private List<ChainNftAttributes> attributes;
    private String image;
}
