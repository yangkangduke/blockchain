package com.seeds.uc.handler;

import cn.hutool.extra.mail.MailException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.seeds.common.dto.GenericDto;
import com.seeds.uc.exceptions.GenericException;
import com.seeds.uc.exceptions.InvalidArgumentsException;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.binding.BindingException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;

/**
 * @author allen
 * @email allen.hua.ai@gmail.com
 * @date 2020/8/29
 */
@Slf4j
@ControllerAdvice
public class UcExceptionHandler {

    @ResponseBody
    @ExceptionHandler(Throwable.class)
    ResponseEntity<GenericDto<String>> handle(Throwable e) {
        log.error("General Exception:", e);
        return new ResponseEntity<>(
                GenericDto.failure("Internal Error", HttpStatus.INTERNAL_SERVER_ERROR.value()),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ResponseBody
    @ExceptionHandler(GenericException.class)
    ResponseEntity<GenericDto<String>> handle(GenericException e) {
        return new ResponseEntity<>(GenericDto.failure(e.getMessage(), e.getErrorCode().getCode()), HttpStatus.OK);
    }

    /**
     * 请求参数类型转换异常（满足Cause是InvalidFormatException异常类型的条件），不需要打印堆栈信息
     * <p>{@link HttpMessageNotReadableException} && {@link InvalidFormatException}</p>
     * @param e
     * @return
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<GenericDto<String>> handleResultException(HttpMessageNotReadableException e) {
        if(e.getCause() instanceof InvalidFormatException) {
            return new ResponseEntity<>(
                    GenericDto.failure("JSON parse error", HttpStatus.BAD_REQUEST.value()),
                    HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<>(
                    GenericDto.failure("Internal Error：" + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ResponseBody
    @ExceptionHandler(NumberFormatException.class)
    ResponseEntity<GenericDto<String>> handle(NumberFormatException e) {
        return new ResponseEntity<>(
                GenericDto.failure("String cannot be converted to number", HttpStatus.BAD_REQUEST.value()),
                HttpStatus.BAD_REQUEST);
    }

    @ResponseBody
    @ExceptionHandler(MailException.class)
    ResponseEntity<GenericDto<String>> handle(MailException e) {
        return new ResponseEntity<>(
                GenericDto.failure(e.getMessage(), HttpStatus.BAD_REQUEST.value()),
                HttpStatus.BAD_REQUEST);
    }

    @ResponseBody
    @ExceptionHandler(InvalidArgumentsException.class)
    ResponseEntity<GenericDto<String>> handle(InvalidArgumentsException e) {
        return new ResponseEntity<>(
                GenericDto.failure(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY.value()),
                HttpStatus.UNPROCESSABLE_ENTITY);
    }

    //spring-context包里面的异常
    //实体对象前不加@RequestBody注解,单个对象内属性校验未通过抛出的异常类型
    @ExceptionHandler(BindingException.class)
    public ResponseEntity<GenericDto<String>> handle(BindingException e) {
        return new ResponseEntity<>(
                GenericDto.failure(e.getMessage(), HttpStatus.BAD_REQUEST.value()),
                HttpStatus.BAD_REQUEST);
    }

    //spring-context包里面的异常,实体对象前加@RequestBody注解,抛出的异常为该类异常
    //方法参数如果带有@RequestBody注解，那么spring mvc会使用RequestResponseBodyMethodProcessor      //对参数进行序列化,并对参数做校验
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<GenericDto<String>> handle(MethodArgumentNotValidException e) {
        return new ResponseEntity<>(
                GenericDto.failure(e.getBindingResult().getFieldError().getField() + ":" + e.getBindingResult().getFieldError().getDefaultMessage(), HttpStatus.BAD_REQUEST.value()),
                HttpStatus.BAD_REQUEST);
    }

    //实体对象前不加@RequestBody注解,校验方法参数或方法返回值时,未校验通过时抛出的异常
    //Validation-api包里面的异常
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<GenericDto<String>> handle(ValidationException e) {
        return new ResponseEntity<>(
                GenericDto.failure(e.getCause().getMessage(), HttpStatus.BAD_REQUEST.value()),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<GenericDto<String>> handle(ConstraintViolationException e) {
        return new ResponseEntity<>(
                GenericDto.failure(e.getCause().getMessage(), HttpStatus.BAD_REQUEST.value()),
                HttpStatus.BAD_REQUEST);
    }


}