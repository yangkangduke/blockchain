package com.seeds.admin.aspect;

import com.seeds.admin.annotation.AuditLog;
import com.seeds.admin.audit.Auditable;
import com.seeds.admin.entity.SysAuditLog;
import com.seeds.admin.service.ISysAuditLogService;
import com.seeds.common.dto.GenericDto;
import com.seeds.common.web.context.UserContext;
import com.seeds.common.web.exception.AuthException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Component
@Aspect
@Slf4j
@Order(value = 2)
public class AuditLogAspect {

    @Autowired
    private ISysAuditLogService sysAuditLogService;
//    @Autowired
//    private NotificationService notificationService;

    @Around(value = "@annotation(auditLog)")
    @Transactional
    public Object doAround(ProceedingJoinPoint joinPoint, AuditLog auditLog) throws Throwable {
        Object res = joinPoint.proceed();
        if(res instanceof GenericDto && ((GenericDto) res).isSuccess()) {
            Object[] args = joinPoint.getArgs();
            Auditable auditable = (Auditable) args[0];
            Long currentAdminUserId = UserContext.getCurrentAdminUserId();
            if (currentAdminUserId == null) {
                throw new AuthException("unknown user");
            }
            if (isNotBlank(auditable.generateAuditKey())) {
                try {
                    sysAuditLogService.save(SysAuditLog.builder()
                            .action(auditLog.action())
                            .module(auditLog.module())
                            .subModule(auditLog.subModule())
                            .userId(currentAdminUserId)
                            // todo
                            .userName("")
                            .dataKey(auditable.generateAuditKey())
                            .afterChange(auditable.getAuditData())
                            // todo
                            .ip("")
                            .build());
                } catch (Exception exception) {
                    log.error("insert audit log data with {} failure, due to {}", auditable.getAuditData(), exception.getMessage(), exception);
                }
            }
            // todo
//            notificationService.sendNotice(getNotificationRequest(auditable, auditLog, currentUser));
        }
        return res;
    }



}
