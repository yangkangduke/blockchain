package com.seeds.admin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seeds.admin.entity.SysAuditLog;
import com.seeds.admin.mapper.SysAuditLogMapper;
import com.seeds.admin.service.ISysAuditLogService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * sys audit log table 服务实现类
 * </p>
 *
 * @author yk
 * @since 2022-10-25
 */
@Service
public class SysAuditLogServiceImpl extends ServiceImpl<SysAuditLogMapper, SysAuditLog> implements ISysAuditLogService {

}
