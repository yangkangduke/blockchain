package com.seeds.admin.dto;

import lombok.Data;

import java.util.List;

/**
 * @author: hewei
 * @date 2023/2/20
 */
@Data
public class SysNFTAttrJSONDto {
    private String name;
    private String symbol;
    private String description;
    private String image;
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
