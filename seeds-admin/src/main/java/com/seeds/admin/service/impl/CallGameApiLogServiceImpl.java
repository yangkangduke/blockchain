package com.seeds.admin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seeds.admin.service.CallGameApiLogService;
import com.seeds.game.entity.CallGameApiErrorLogEntity;
import com.seeds.admin.mapper.CallGameApiLogMapper;
import org.springframework.stereotype.Service;

/**
 * @author: hewei
 * @date 2023/3/6
 */
@Service
public class CallGameApiLogServiceImpl extends ServiceImpl<CallGameApiLogMapper, CallGameApiErrorLogEntity> implements CallGameApiLogService {

}
