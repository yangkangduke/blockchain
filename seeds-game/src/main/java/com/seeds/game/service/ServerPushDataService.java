package com.seeds.game.service;


import com.seeds.game.dto.request.OpenHeroRecordReq;
import com.seeds.game.dto.request.OpenMatchRecordReq;

/**
 * <p>
 * 游戏服推数据 服务类
 * </p>
 *
 * @author hang.yu
 * @since 2023-02-13
 */
public interface ServerPushDataService {

    /**
     * 对局记录
     * @param matchRecord 对局记录
     */
    void matchRecord(OpenMatchRecordReq matchRecord);

    /**
     * 英雄记录
     * @param heroRecordReq 英雄记录
     */
    void heroRecord(OpenHeroRecordReq heroRecordReq);

}
