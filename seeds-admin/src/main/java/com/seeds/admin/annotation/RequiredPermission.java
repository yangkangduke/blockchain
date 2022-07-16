package com.seeds.admin.annotation;

import java.lang.annotation.*;

/**
 * 需要权限注解
 *
 * @author hang.yu
 * @date 2022/7/16
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequiredPermission {

    String[] value();

}