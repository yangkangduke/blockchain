//package com.seeds.account.aspect;
//
//import com.dangdang.ddframe.job.api.ShardingContext;
//import com.seeds.account.service.TaskMonitorService;
//import lombok.extern.slf4j.Slf4j;
//import org.aspectj.lang.ProceedingJoinPoint;
//import org.aspectj.lang.annotation.Around;
//import org.aspectj.lang.annotation.Aspect;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.core.annotation.Order;
//import org.springframework.stereotype.Component;
//
//
///**
// * @author ray
// */
//@Component
//@Aspect
//@Slf4j
//@Order(value = 1)
//public class TaskMonitorAspect {
//
//    @Autowired
//    TaskMonitorService taskMonitorService;
//
//    @Around(value = "execution(* com.seeds.account.task.*.execute(..))")
//    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
//        ShardingContext shardingContext = joinPoint.getArgs() != null ? (ShardingContext) joinPoint.getArgs()[0] : null;
//
//        Object result = null;
//        try {
//            result = joinPoint.proceed();
//            taskMonitorService.success(shardingContext);
//            return result;
//        } catch (Throwable throwable) {
//            taskMonitorService.error(shardingContext);
//            throw throwable;
//        }
//    }
//
//    @Around(value = "execution(* com.seeds.account.task.affiliate.*.execute(..))")
//    public Object doAffiliateTaskAround(ProceedingJoinPoint joinPoint) throws Throwable {
//        ShardingContext shardingContext = joinPoint.getArgs() != null ? (ShardingContext) joinPoint.getArgs()[0] : null;
//
//        Object result = null;
//        try {
//            result = joinPoint.proceed();
//            taskMonitorService.success(shardingContext);
//            return result;
//        } catch (Throwable throwable) {
//            taskMonitorService.error(shardingContext);
//            throw throwable;
//        }
//    }
//}
