package com.seeds.uc.aspect;


import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.seeds.common.dto.GenericDto;
import com.seeds.common.web.HttpHeaders;
import com.seeds.common.web.context.UserContext;
import com.seeds.common.web.exception.AuthException;
import com.seeds.uc.annotation.UcOpLog;
import com.seeds.uc.model.UcOperationLog;
import com.seeds.uc.model.UcUser;
import com.seeds.uc.service.IUcOperationLogService;
import com.seeds.uc.service.IUcUserService;
import com.seeds.uc.util.WebUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Objects;

/**
 * UC操作日志，只记录从游戏方过来的请求日志，web端的请求不记录
 * 请求头里面是否包含 服务器编号:server-no 来区分是否来自游戏方
 *
 * @author hewei
 * @date 2022/1/12
 */
@Aspect
@Component
public class UcOpLogAspect {

    @Resource
    private IUcOperationLogService logService;
    @Resource
    private IUcUserService ucUserService;


    @Pointcut("@annotation(com.seeds.uc.annotation.UcOpLog)")
    public void ucLogPoinCut() {

    }

    @Around("ucLogPoinCut()")
    public Object saveSysLog(ProceedingJoinPoint joinPoint) throws Throwable {

        // 请求时间
        long opTime = System.currentTimeMillis();

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        String serverNo = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getHeader(HttpHeaders.SERVER_NO);
        Object result = joinPoint.proceed();
        // 请求头没有serverNo，则请求来自web端，直接返回
//        if (Objects.isNull(serverNo)) {
//            return GenericDto.success(result);
//        }
        //保存日志
        UcOperationLog operationLog = new UcOperationLog();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        UcOpLog operation = method.getAnnotation(UcOpLog.class);
        if (operation != null) {
            String value = operation.value();
            operationLog.setOpDesc(value);
        }
        //请求类型
        operationLog.setMethod(request.getMethod());
        //请求的参数
        Object[] args = joinPoint.getArgs();
        //将参数转换成json
        String params = JSONUtil.toJsonStr(args);

        try {
            Long userId = UserContext.getCurrentUserId();
            UcUser user = ucUserService.getById(userId);
            operationLog.setUser(user.getEmail());
        } catch (AuthException e) {
            // 登陆请求获取不到用户上下文对象
            operationLog.setUser("");
        }
        // 请求结果
        int code = this.readValue(JSONUtil.toJsonStr(result), "code");
        operationLog.setOpStatus(code == 200 ? "success" : "fail");
        operationLog.setServerNo(serverNo);
        operationLog.setParams(params == null ? "" : params);
        operationLog.setUrl(request.getRequestURL().toString());
        operationLog.setResult(JSONUtil.toJsonStr(result));
        operationLog.setUserIp(WebUtil.getIpAddr(request));
        operationLog.setOpTime(opTime);
        // 执行耗时
        operationLog.setSpendTime(System.currentTimeMillis() - opTime);
        operationLog.setCreatedAt(System.currentTimeMillis());
        logService.save(operationLog);
        return GenericDto.success(result);
    }

    private int readValue(String jsonStr, String key) {
        int value = 0;
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = null;
        try {
            jsonNode = objectMapper.readTree(jsonStr);
            JsonNode keyNode = jsonNode.get(key);
            value = keyNode.asInt();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return value;
    }
}
