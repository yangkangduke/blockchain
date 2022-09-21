package com.seeds.wallet.handler;

import com.seeds.common.dto.GenericDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class WalletExceptionHandler {

    public static final int ERR_CODE = 500;

    @ExceptionHandler()
    public ResponseEntity exceptionHandle(Exception e) {
        log.error("", e);
        return ResponseEntity.ok(GenericDto.failure(e.getMessage(), ERR_CODE));
    }
}
