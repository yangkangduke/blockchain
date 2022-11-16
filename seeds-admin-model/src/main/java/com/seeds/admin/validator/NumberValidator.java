package com.seeds.admin.validator;

import com.seeds.admin.annotation.MgtNumberValidator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.math.BigDecimal;

public class NumberValidator implements ConstraintValidator<MgtNumberValidator,Object> {
    private boolean isEqualZero;
    @Override
    public void initialize(MgtNumberValidator constraintAnnotation) {
        isEqualZero = constraintAnnotation.isEqualZero();
    }

    @Override
    public boolean isValid(Object o, ConstraintValidatorContext constraintValidatorContext) {
        BigDecimal value = null;
        if(o instanceof String) {
            value = new BigDecimal((String) o);
        }
        boolean result = value != null;
        if(isEqualZero) {
            result = value.signum() >= 0;
        } else {
            result = value.signum() == 1;
        }
        return result && value.scale() <= 6;
    }

}
