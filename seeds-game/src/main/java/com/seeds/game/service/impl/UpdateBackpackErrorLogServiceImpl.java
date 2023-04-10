package com.seeds.game.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seeds.game.entity.UpdateBackpackErrorLog;
import com.seeds.game.mapper.UpdateBackpackErrorLogMapper;
import com.seeds.game.service.IUpdateBackpackErrorLogService;
import org.springframework.stereotype.Service;

/**
 * @author: hewei
 * @date 2023/4/5
 */
@Service
public class UpdateBackpackErrorLogServiceImpl extends ServiceImpl<UpdateBackpackErrorLogMapper, UpdateBackpackErrorLog> implements IUpdateBackpackErrorLogService {
    @Override
    public UpdateBackpackErrorLog queryByMintAddress(String mintAddress) {
        return getOne(new LambdaQueryWrapper<UpdateBackpackErrorLog>()
                .eq(UpdateBackpackErrorLog::getMintAddress, mintAddress));

    }
}
