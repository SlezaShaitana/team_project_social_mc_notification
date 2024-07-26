package com.social.mcnotification.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler({SettingsAlreadyCreatedException.class})
    protected ResponseEntity<ErrorMessages> handleException(SettingsAlreadyCreatedException e) {
        log.error("Application specific error handling", e);
        return new ResponseEntity<>(new ErrorMessages(e.getMessage()), HttpStatus.CONFLICT);
    }

}
