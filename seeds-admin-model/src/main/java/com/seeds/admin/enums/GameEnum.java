package com.seeds.admin.enums;

import lombok.Getter;

/**
 * 游戏枚举
 */
@Getter
public enum GameEnum {


    BLADERITE(1L,"blade_rite");

    private Long code;
    private String GameName;

    GameEnum(Long code, String gameName){
        this.code = code;
        this.GameName = gameName;
    }
}
