package com.seeds.admin.dto;

import lombok.Data;

/**
 * @author: hewei
 * @date 2023/2/20
 */
@Data
public class SysNFTAutoIdDto {
    /**
     * 图片名字
     */
    private String pictureName;

    private Long autoId;

    private Long confId;
}
