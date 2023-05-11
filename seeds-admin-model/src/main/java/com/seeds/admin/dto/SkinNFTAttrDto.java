package com.seeds.admin.dto;

import lombok.Data;

import java.util.List;

/**
 * @author: hewei
 * @date 2023/2/20
 */
@Data
public class SkinNFTAttrDto {
    private String name;
    private String symbol = "SEEDS";
    private String description = "This is the NFT of the Seeds skin.";
    private int seller_fee_basis_points = 0;
    private String image;
    private String external_url = "https://www.theseeds.io/";
    private List<Attributes> attributes;
    private Properties properties;

    @Data
    public static class Properties {

        private List<Files> files;

        @Data
        public static class Files {
            private String uri;
            private String type;
        }

    }

    @Data
    public static class Attributes {
        private String trait_type;
        private String value;
    }
}
