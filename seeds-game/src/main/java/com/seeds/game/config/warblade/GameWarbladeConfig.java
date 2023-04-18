package com.seeds.game.config.warblade;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @author hang.yu
 * @date 2023/3/28
 */
@Data
@Configuration
public class GameWarbladeConfig {

    @Value("${game.warblade.player.win.rank.api:/BladeRiteGame/ranking/info.do}")
    private String playerWinRank;

}
