package com.seeds.account.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

/**
 *
 * 定义唯一分布式锁，只有拥有锁的人可以执行, 得不到锁的人可以选择等待或者直接返回
 *
 * @author yk
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface SingletonLock {

    String key() default "";

    long waitTime() default -1L;

    long leaseTime() default 0L;

    TimeUnit unit() default TimeUnit.MILLISECONDS;
}
