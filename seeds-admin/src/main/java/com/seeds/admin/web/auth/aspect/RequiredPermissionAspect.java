package com.seeds.admin.web.auth.aspect;

import com.seeds.admin.annotation.RequiredPermission;
import com.seeds.admin.web.auth.service.AdminShiroService;
import com.seeds.common.web.context.UserContext;
import com.seeds.common.web.exception.PermissionException;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Set;


/**
 * 权限鉴定，切面处理类
 *
 * @author hang.yu
 * @date 2022/7/16
 */
@Aspect
@Component
public class RequiredPermissionAspect {

    @Autowired
    private AdminShiroService adminShiroService;

    @Pointcut("@annotation(com.seeds.admin.annotation.RequiredPermission)")
    public void requiredPermissionCut() {

    }

    @Before("requiredPermissionCut()")
    public void requiredPermission(JoinPoint point) {
        // 获取登录用户
        Long userId = UserContext.getCurrentAdminUserId();
        // 用户权限列表
        try {
            Set<String> permsSet = adminShiroService.getUserPermissions(userId);
            boolean allowed = identification(point, permsSet);
            if (!allowed) {
                throw new PermissionException("No permission, access not allowed");
            }
        } catch (Exception e) {
            throw new PermissionException("No permission, access not allowed");
        }
    }

    /**
     * 鉴权
     */
    private boolean identification(JoinPoint joinPoint, Set<String> permsSet) throws Exception {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = joinPoint.getTarget().getClass().getDeclaredMethod(signature.getName(), signature.getParameterTypes());
        RequiredPermission requiredPermission = method.getAnnotation(RequiredPermission.class);
        String[] values = requiredPermission.value();
        for (String value: values) {
            if (!permsSet.contains(value)) {
                return false;
            }
        }
        return true;
    }
}