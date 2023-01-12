package com.seeds.uc.annotation;

import java.lang.annotation.*;

/**
 * 记录系统操作日志
 *
 * @author hewei
 * @date 2023/1/12
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface UcOpLog {
    String value() default "";
}
