package com.seeds.uc.handler;

import com.seeds.common.dto.GenericDto;
import com.seeds.uc.exceptions.GenericException;
import com.seeds.uc.exceptions.InvalidArgumentsException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

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

    @ResponseBody
    @ExceptionHandler(InvalidArgumentsException.class)
    ResponseEntity<GenericDto<String>> handle(InvalidArgumentsException e) {
        return new ResponseEntity<>(
                GenericDto.failure(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY.value()),
                HttpStatus.UNPROCESSABLE_ENTITY);
    }
}