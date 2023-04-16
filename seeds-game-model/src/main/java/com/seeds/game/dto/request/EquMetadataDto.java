package com.seeds.game.dto.request;

import lombok.Data;

import java.util.List;

/**
 * 装备metadata
 *
 * @author: hewei
 * @date 2023/4/15
 */
@Data
public class EquMetadataDto {
    private String name;
    private String symbol = "SEQN";
    private String description = "This is the NFT of the seeds equipment.";
    private int seller_fee_basis_points = 0;
    private String image;
    private String external_url = "https://www.theseeds.io/";
    private List<EquMetadataDto.Attributes> attributes;
    private EquMetadataDto.Properties properties;

    @Data
    public static class Properties {
        private List<EquMetadataDto.Properties.Files> files;

        @Data
        public static class Files {
            private String type;
            private String uri;
        }
    }

    @Data
    public static class Attributes {
        private String trait_type;
        private String value;
    }
}
