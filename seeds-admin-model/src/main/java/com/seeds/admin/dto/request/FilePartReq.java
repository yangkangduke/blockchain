package com.seeds.admin.dto.request;

import lombok.Data;

/**
 * @author: hewei
 * @date 2023/1/3
 */
@Data
public class FilePartReq {
    private String key;
    private String uploadId;
    private Integer partNumber;
}
