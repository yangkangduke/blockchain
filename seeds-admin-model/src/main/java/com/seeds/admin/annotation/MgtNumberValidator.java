package com.seeds.admin.annotation;
import com.seeds.admin.validator.NumberValidator;

import javax.validation.Payload;
import javax.validation.Constraint;
import java.lang.annotation.*;

@Documented
@Target({ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = NumberValidator.class)
public @interface MgtNumberValidator {

    String message() default "param less than zero or scale more than 6";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    boolean isEqualZero() default false;
}
