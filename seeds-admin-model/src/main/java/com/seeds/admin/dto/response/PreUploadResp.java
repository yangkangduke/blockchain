package com.seeds.admin.dto.response;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author: hewei
 * @date 2022/12/29
 */
@ApiModel(value = "PreUploadResp", description = "游戏资源信息")
@Data
@Accessors(chain = true)
public class PreUploadResp {

    private String key;

    private String url;

    private String uploadId;
}
