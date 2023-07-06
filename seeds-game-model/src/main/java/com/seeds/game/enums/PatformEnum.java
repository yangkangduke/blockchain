package com.seeds.game.enums;

import lombok.Getter;

/**
 * @author: he.wei
 * @date 2023/7/6
 */
@Getter
public enum PatformEnum {

    //1,Magic Eden  2,Seeds 3,game (游戏内)
    MAGIC_EDEN(1),
    SEEDS(2),
    GAME(3);

    private int code;

    PatformEnum(int code) {
        this.code = code;
    }
}
