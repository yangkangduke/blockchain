package com.seeds.admin.mapstruct;

import org.mapstruct.Named;

import java.math.BigDecimal;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

public interface BaseMapstructMapper {

    @Named("convertBigDecimalToStringWithoutDot")
    default String convertBigDecimalToStringWithoutDot(BigDecimal val){
        return val == null ? null : val.setScale(0, BigDecimal.ROUND_DOWN).toPlainString();
    }

    @Named("convertBigDecimalToStringWith4")
    default String convertBigDecimalToStringWith4(BigDecimal val){
        return val == null ? null : val.setScale(4, BigDecimal.ROUND_DOWN).toPlainString();
    }

    @Named("convertBigDecimalToString")
    default String convertBigDecimalToString(BigDecimal val){
        return val == null ? null : val.setScale(6, BigDecimal.ROUND_DOWN).toPlainString();
    }

    @Named("convertStringToBigDecimal")
    default BigDecimal convertStringToBigDecimal(String val){
        return isNotBlank(val) ? new BigDecimal(val) : null;
    }


    @Named("convertDoubleToBigDecimal")
    default BigDecimal convertDoubleToBigDecimal(double val) {
        return BigDecimal.valueOf(val).setScale(4, BigDecimal.ROUND_DOWN);
    }

    @Named("convertBigDecimalToDouble")
    default double convertBigDecimalToDouble(BigDecimal val) {
        return val == null ? BigDecimal.ZERO.doubleValue() : val.doubleValue();
    }

    @Named("convertBigDecimalTo18DigitString")
    default String convertBigDecimalTo18DigitString(BigDecimal val){
        return val == null ? null : val.setScale(18, BigDecimal.ROUND_DOWN).toPlainString();
    }

}
