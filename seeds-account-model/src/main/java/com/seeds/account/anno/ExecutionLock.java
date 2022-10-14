package com.seeds.account.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

/**
 *
 * Redis锁，操作需要等锁才能执行
 *
 * @author yk
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface ExecutionLock {
    String key() default "";

    long waitTime() default 60L;

    long leaseTime() default 300L;

    TimeUnit unit() default TimeUnit.SECONDS;
}
