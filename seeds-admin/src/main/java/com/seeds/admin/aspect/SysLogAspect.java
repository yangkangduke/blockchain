package com.seeds.admin.aspect;


import cn.hutool.json.JSONUtil;
import com.seeds.admin.annotation.SeedsOperationLog;
import com.seeds.admin.entity.SysLogEntity;
import com.seeds.admin.entity.SysUserEntity;
import com.seeds.admin.service.SysLogService;
import com.seeds.admin.service.SysUserService;
import com.seeds.admin.utils.IPUtil;
import com.seeds.common.web.context.UserContext;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

/**
 * 系统操作日志
 *
 * @author hewei
 * @date 2022/7/26
 */
@Aspect
@Component
@Slf4j
public class SysLogAspect {

    @Resource
    private SysLogService sysLogService;
    @Resource
    private SysUserService sysUserService;

    @Pointcut("@annotation(com.seeds.admin.annotation.SeedsOperationLog)")
    public void logPoinCut() {

    }

    @AfterReturning("logPoinCut()")
    public void saveSysLog(JoinPoint joinPoint) {

        //保存日志
        SysLogEntity sysLog = new SysLogEntity();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        SeedsOperationLog operation = method.getAnnotation(SeedsOperationLog.class);
        if (operation != null) {
            String value = operation.value();
            sysLog.setOperation(value);//保存获取的操作

        }
        //获取请求的类名
        String className = joinPoint.getTarget().getClass().getName();
        //获取请求的方法名
        String methodName = method.getName();
        sysLog.setMethod(className + "." + methodName);
        //请求的参数
        Object[] args = joinPoint.getArgs();
        //将参数转换成json
        String params = null;
        try {
            params = JSONUtil.toJsonStr(args);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (params == null) {
            sysLog.setParams("无参数");
        } else {
            sysLog.setParams(params);
        }

        //获取用户真实ip地址
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        sysLog.setIp(IPUtil.getIpAddress(request));
        Long userId = UserContext.getCurrentAdminUserId();
        sysLog.setOperator(userId);
        SysUserEntity sysUser = sysUserService.queryById(userId);
        if (!ObjectUtils.isEmpty(sysUser)) {
            sysLog.setOperatorName(sysUser.getRealName());
        }
        sysLogService.save(sysLog);
    }
}
