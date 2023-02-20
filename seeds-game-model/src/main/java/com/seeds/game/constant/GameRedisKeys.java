package com.seeds.game.constant;

import lombok.experimental.UtilityClass;

@UtilityClass
public class GameRedisKeys {

    public final String GAME_KEY_PREFIX = "game:";

    public final String GAME_HERO_RANK_KEY_TEMPLATE  = GAME_KEY_PREFIX + "gameServer%s:" + "hero%s";

    /**
     * 拼接管理后台用户token的key
     */
    public String getGameHeroRankKey(String gameServerId, Long heroId) {
        return String.format(GAME_HERO_RANK_KEY_TEMPLATE, gameServerId, heroId);
    }

}