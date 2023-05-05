package com.seeds.game.handler;

import com.seeds.admin.exceptions.GenericException;
import com.seeds.admin.exceptions.InvalidArgumentsException;
import com.seeds.common.dto.GenericDto;
import com.seeds.common.web.exception.PermissionException;
import com.seeds.game.enums.GameErrorCodeEnum;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.binding.BindingException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;

/**
* @author yk
 * @date 2020/8/29
 */
@Slf4j
@ControllerAdvice
public class GameExceptionHandler {

    @ResponseBody
    @ExceptionHandler(Exception.class)
    ResponseEntity<GenericDto<String>> handle(Exception e) {
        log.error("Exception:", e);
        return new ResponseEntity<>(
                GenericDto.failure("Internal Error:" + GameErrorCodeEnum.ERR_500_SYSTEM_BUSY.getDescEn(), HttpStatus.INTERNAL_SERVER_ERROR.value()),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ResponseBody
    @ExceptionHandler(FeignException.class)
    ResponseEntity<GenericDto<String>> handle(FeignException e) {
        log.error("FeignException:", e);
        return new ResponseEntity<>(GenericDto.failure(GameErrorCodeEnum.ERR_505_INTERNAL_FAILURE.getDescEn(), HttpStatus.INTERNAL_SERVER_ERROR.value()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ResponseBody
    @ExceptionHandler(GenericException.class)
    ResponseEntity<GenericDto<String>> handle(GenericException e) {
        log.error("GenericException:", e);
        return new ResponseEntity<>(GenericDto.failure(e.getMessage(), e.getErrorCode().getCode()), HttpStatus.OK);
    }

    @ResponseBody
    @ExceptionHandler(InvalidArgumentsException.class)
    ResponseEntity<GenericDto<String>> handle(InvalidArgumentsException e) {
        log.error("InvalidArguments Exception:", e);
        return new ResponseEntity<>(
                GenericDto.failure(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY.value()),
                HttpStatus.UNPROCESSABLE_ENTITY);
    }

    //spring-context包里面的异常
    //实体对象前不加@RequestBody注解,单个对象内属性校验未通过抛出的异常类型
    @ExceptionHandler(BindingException.class)
    public ResponseEntity<GenericDto<String>> handle(BindingException e) {
        log.error("Binding Exception:", e);
        return new ResponseEntity<>(
                GenericDto.failure("Internal Error:" + GameErrorCodeEnum.ERR_500_SYSTEM_BUSY.getDescEn(), HttpStatus.BAD_REQUEST.value()),
                HttpStatus.BAD_REQUEST);
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
        return new ResponseEntity<>(
                GenericDto.failure(e.getBindingResult().getFieldError().getField() + ":" + e.getBindingResult().getFieldError().getDefaultMessage(), HttpStatus.BAD_REQUEST.value()),
                HttpStatus.BAD_REQUEST);
    }

    //实体对象前不加@RequestBody注解,校验方法参数或方法返回值时,未校验通过时抛出的异常
    //Validation-api包里面的异常
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<GenericDto<String>> handle(ValidationException e){
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


    @ResponseBody
    @ExceptionHandler(com.seeds.game.exception.GenericException.class)
    ResponseEntity<GenericDto<String>> handle(com.seeds.game.exception.GenericException e) {
        log.error("General Exception:", e);
        return new ResponseEntity<>(
                GenericDto.failure(e.getMessage(), e.getErrorCode().getCode()),
                HttpStatus.OK);
    }

    /**
     * System exception
     *
     * @param request
     * @param e
     * @return
     */
    @ResponseBody
    @ExceptionHandler(PermissionException.class)
    ResponseEntity<GenericDto> handle(HttpServletRequest request, PermissionException e) {
        return new ResponseEntity<>(GenericDto.failure("permission error: " + e.getMessage(), 403), HttpStatus.OK);
    }

}
