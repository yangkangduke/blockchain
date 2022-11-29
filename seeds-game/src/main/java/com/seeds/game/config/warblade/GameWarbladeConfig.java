package com.seeds.game.config.warblade;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 游戏Warblade配置
 *
 * @author hang.yu
 * @since 1.0.0
 */
@Slf4j
@Data
@Component
@ConfigurationProperties(prefix = "game-warblade")
public class GameWarbladeConfig {

    String nftNotificationApi;

}
