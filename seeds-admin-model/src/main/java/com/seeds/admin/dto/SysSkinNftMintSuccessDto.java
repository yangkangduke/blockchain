package com.seeds.admin.dto;

import lombok.Data;

import java.util.List;

/**
 * @author: hewei
 * @date 2023/4/26
 */
@Data
public class SysSkinNftMintSuccessDto {

    private List<SkinNftMintSuccess> successList;

    @Data
    public static class SkinNftMintSuccess {
        private String mintAddress;
        private String name;
        private String tokenAddress;
        private String signature;
        private String owner;
    }
}
