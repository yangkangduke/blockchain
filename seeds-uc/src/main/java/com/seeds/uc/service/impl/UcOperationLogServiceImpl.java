package com.seeds.uc.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seeds.uc.mapper.UcOperationLogMapper;
import com.seeds.uc.model.UcOperationLog;
import com.seeds.uc.service.IUcOperationLogService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * uc操作日志表 服务实现类
 * </p>
 *
 * @author hewei
 * @since 2023-01-12
 */
@Service
public class UcOperationLogServiceImpl extends ServiceImpl<UcOperationLogMapper, UcOperationLog> implements IUcOperationLogService {

}
