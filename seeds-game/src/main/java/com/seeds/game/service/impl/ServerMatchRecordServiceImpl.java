package com.seeds.game.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seeds.game.entity.ServerMatchRecordEntity;
import com.seeds.game.mapper.ServerMatchRecordMapper;
import com.seeds.game.service.IServerMatchRecordService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 游戏服对局记录 服务实现类
 * </p>
 *
 * @author hang.yu
 * @since 2023-02-13
 */
@Service
public class ServerMatchRecordServiceImpl extends ServiceImpl<ServerMatchRecordMapper, ServerMatchRecordEntity> implements IServerMatchRecordService {

}
