package com.seeds.admin.annotation;


import com.seeds.admin.enums.Action;
import com.seeds.admin.enums.Module;
import com.seeds.admin.enums.SubModule;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface AuditLog {

    Module module() default Module.UNKNOWN;

    SubModule subModule() default SubModule.UNKNOWN;

    Action action() default Action.UNKNOWN;
}
