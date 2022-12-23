package com.seeds.admin.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author: hewei
 * @date 2022/12/21
 */
@Getter
@AllArgsConstructor
public enum GameSrcTypeEnum {

    MAIN_VIDEO(1, "首页视频"),
    INSTALL_PK(2, "安装包"),
    PATCH_PK(3, "补丁包");

    private Integer code;
    private String name;


    public static String getNameByCode(Integer code) {
        for (GameSrcTypeEnum value : GameSrcTypeEnum.values()) {
            if (value.getCode().equals(code)) {
                return value.getName();
            }
        }
        return null;
    }

}
