package com.seeds.admin.annotation;

import java.lang.annotation.*;

/**
 * 数据过滤注解
 *
 * @author hang.yu
 * @date 2022/7/25
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataFilter {

    /**
     * 表的别名
     */
    String tableAlias() default "";

    /**
     * 用户ID
     */
    String userId() default "createdBy";

    /**
     * 部门ID
     */
    String deptId() default "dept_id";

}