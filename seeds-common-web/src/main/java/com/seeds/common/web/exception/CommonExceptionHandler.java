package com.seeds.common.web.exception;

import com.seeds.common.dto.GenericDto;
import com.seeds.common.exception.SeedsException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@ControllerAdvice
public class CommonExceptionHandler {

    /**
     * Unknown exception
     *
     * @param request
     * @param e
     * @return
     */
    @ResponseBody
    @ExceptionHandler(Throwable.class)
    ResponseEntity<GenericDto> handle(HttpServletRequest request, Throwable e) {
        log.error("Unknown exception caused", e);
        return new ResponseEntity<>(GenericDto.failure("Unknown exception caused: " + e.getMessage(), 500), HttpStatus.OK);
    }

    /**
     * System exception
     *
     * @param request
     * @param e
     * @return
     */
    @ResponseBody
    @ExceptionHandler(SeedsException.class)
    ResponseEntity<GenericDto> handle(HttpServletRequest request, SeedsException e) {
        log.error("System exception caused", e);
        return new ResponseEntity<>(GenericDto.failure("System exception caused: " + e.getMessage(), 500), HttpStatus.OK);
    }

    /**
     * System exception
     *
     * @param request
     * @param e
     * @return
     */
    @ResponseBody
    @ExceptionHandler(AuthException.class)
    ResponseEntity<GenericDto> handle(HttpServletRequest request, AuthException e) {
        return new ResponseEntity<>(GenericDto.failure("Auth error: " + e.getMessage(), 401), HttpStatus.OK);
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

    /**
     * Validate exception
     *
     * @param request
     * @param e
     * @return
     */
    @ResponseBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<GenericDto> handle(HttpServletRequest request, MethodArgumentNotValidException e) {
        FieldError fieldError = e.getBindingResult().getFieldError();
        String message;
        if (fieldError == null) {
            message = "Parameter check failed";
        } else {
            message = fieldError.getDefaultMessage();
        }
        return new ResponseEntity<>(GenericDto.failure("permission error: " + message, 500), HttpStatus.OK);
    }
}
