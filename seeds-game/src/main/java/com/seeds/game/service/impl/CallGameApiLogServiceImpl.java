package com.seeds.game.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seeds.game.entity.CallGameApiErrorLogEntity;
import com.seeds.game.mapper.CallGameApiLogMapper;
import com.seeds.game.service.CallGameApiLogService;
import org.springframework.stereotype.Service;

/**
 * @author: hewei
 * @date 2023/3/6
 */
@Service
public class CallGameApiLogServiceImpl extends ServiceImpl<CallGameApiLogMapper, CallGameApiErrorLogEntity> implements CallGameApiLogService {

}
