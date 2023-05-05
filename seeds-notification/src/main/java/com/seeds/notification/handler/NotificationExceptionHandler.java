package com.seeds.notification.handler;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.seeds.common.dto.GenericDto;
import com.seeds.notification.enums.NoticeErrorCodeEnum;
import com.seeds.notification.exceptions.GenericException;
import com.seeds.notification.exceptions.InvalidArgumentsException;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.binding.BindingException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;

/**
* @author yk
 * @date 2020/8/29
 */
@Slf4j
@ControllerAdvice
public class NotificationExceptionHandler {

    @ResponseBody
    @ExceptionHandler(Exception.class)
    ResponseEntity<GenericDto<String>> handle(Exception e) {
        log.error("Exception:", e);
        return new ResponseEntity<>(
                GenericDto.failure("Internal Error:" + NoticeErrorCodeEnum.ERR_500_SYSTEM_BUSY.getDescEn(), HttpStatus.INTERNAL_SERVER_ERROR.value()),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ResponseBody
    @ExceptionHandler(FeignException.class)
    ResponseEntity<GenericDto<String>> handle(FeignException e) {
        log.error("FeignException:", e);
        return new ResponseEntity<>(
                GenericDto.failure(NoticeErrorCodeEnum.ERR_505_INTERNAL_FAILURE.getDescEn(), HttpStatus.INTERNAL_SERVER_ERROR.value()),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ResponseBody
    @ExceptionHandler(GenericException.class)
    ResponseEntity<GenericDto<String>> handle(GenericException e) {
        log.error("General Exception:", e);
        return new ResponseEntity<>(
                GenericDto.failure(e.getMessage(), e.getErrorCode().getCode()),
                HttpStatus.OK);
    }

    /**
     * 请求参数类型转换异常（满足Cause是InvalidFormatException异常类型的条件），不需要打印堆栈信息
     * <p>{@link HttpMessageNotReadableException} && {@link InvalidFormatException}</p>
     *
     * @param e
     * @return
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<GenericDto<String>> handleResultException(HttpMessageNotReadableException e) {
        log.error("HttpMessageNotReadableException Exception:", e);
        if (e.getCause() instanceof InvalidFormatException) {
            return new ResponseEntity<>(
                    GenericDto.failure("JSON parse error", HttpStatus.BAD_REQUEST.value()),
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<>(
                    GenericDto.failure(NoticeErrorCodeEnum.ERR_502_ILLEGAL_ARGUMENTS.getDescEn(), HttpStatus.INTERNAL_SERVER_ERROR.value()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ResponseBody
    @ExceptionHandler(InvalidArgumentsException.class)
    ResponseEntity<GenericDto<String>> handle(InvalidArgumentsException e) {
        log.error("InvalidArguments Exception:", e);
        return new ResponseEntity<>(
                GenericDto.failure(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY.value()),
                HttpStatus.OK);
    }

    //spring-context包里面的异常
    //实体对象前不加@RequestBody注解,单个对象内属性校验未通过抛出的异常类型
    @ExceptionHandler(BindingException.class)
    public ResponseEntity<GenericDto<String>> handle(BindingException e) {
        log.error("Binding Exception:", e);
        return new ResponseEntity<>(
                GenericDto.failure("Internal Error:" + NoticeErrorCodeEnum.ERR_500_SYSTEM_BUSY.getDescEn(), HttpStatus.BAD_REQUEST.value()),
                HttpStatus.OK);
    }

    // 唯一键冲突
    @ExceptionHandler(DuplicateKeyException.class)
    public ResponseEntity<GenericDto<String>> handle(DuplicateKeyException e) {
        log.error("DuplicateKeyException:", e);
        return new ResponseEntity<>(
                GenericDto.failure("please do not submit again!", HttpStatus.BAD_REQUEST.value()),
                HttpStatus.BAD_REQUEST);
    }

    //spring-context包里面的异常,实体对象前加@RequestBody注解,抛出的异常为该类异常
    //方法参数如果带有@RequestBody注解，那么spring mvc会使用RequestResponseBodyMethodProcessor      //对参数进行序列化,并对参数做校验
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<GenericDto<String>> handle(MethodArgumentNotValidException e) {
        log.error("MethodArgumentNotValid Exception:", e);
        return new ResponseEntity<>(
                GenericDto.failure(e.getBindingResult().getFieldError().getField() + ":" + e.getBindingResult().getFieldError().getDefaultMessage(), HttpStatus.BAD_REQUEST.value()),
                HttpStatus.OK);
    }

    //实体对象前不加@RequestBody注解,校验方法参数或方法返回值时,未校验通过时抛出的异常
    //Validation-api包里面的异常
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<GenericDto<String>> handle(ValidationException e) {
        log.error("Validation Exception:", e);
        return new ResponseEntity<>(
                GenericDto.failure(e.getCause().getMessage(), HttpStatus.BAD_REQUEST.value()),
                HttpStatus.OK);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<GenericDto<String>> handle(ConstraintViolationException e) {
        log.error("ConstraintViolation Exception:", e);
        return new ResponseEntity<>(
                GenericDto.failure(e.getCause().getMessage(), HttpStatus.BAD_REQUEST.value()),
                HttpStatus.OK);
    }


}