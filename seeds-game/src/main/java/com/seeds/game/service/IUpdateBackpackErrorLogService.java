package com.seeds.game.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.seeds.game.entity.UpdateBackpackErrorLog;

/**
 * @author hewei
 * @since 2023-4-5
 */
public interface IUpdateBackpackErrorLogService extends IService<UpdateBackpackErrorLog> {

    UpdateBackpackErrorLog queryByMintAddress(String mintAddress);
}
