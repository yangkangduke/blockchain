package com.seeds.admin.annotation;

import java.lang.annotation.*;

/**
 * 记录系统操作日志
 *
 * @author hewei
 * @date 2022/7/26
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SeedsOperationLog {
    String value() default "";
}
