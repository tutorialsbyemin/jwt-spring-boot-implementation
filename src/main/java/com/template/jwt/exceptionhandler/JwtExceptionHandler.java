package com.template.jwt.exceptionhandler;

import com.template.jwt.dto.ResponseDto;
import com.template.jwt.exception.InvalidTokenGenerationException;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Log4j2
public class JwtExceptionHandler {

    @ExceptionHandler(InvalidTokenGenerationException.class)
    public ResponseEntity<?> handlerInvalidTokenGeneration(InvalidTokenGenerationException exception) {
        log.error(exception.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseDto("Token generation failed"));
    }
}
