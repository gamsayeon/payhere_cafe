package com.payhere.cafe.exception;


import com.payhere.cafe.model.CommonResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class CustomExceptionHandler {

    private final Logger logger = LogManager.getLogger(this.getClass());

    @ExceptionHandler(value = {RuntimeException.class})
    public ResponseEntity<Object> handleException(RuntimeException ex, HttpServletRequest request) {
        CommonResponse commonResponse = new CommonResponse(CommonResponse.Meta.builder().code(400).message(ex.getMessage()).build()
                , request.getServletPath());
        logger.error(commonResponse.toString());
        return ResponseEntity.badRequest().body(commonResponse);
    }

    @ExceptionHandler(value = {CafeCommonException.class})
    public ResponseEntity<Object> handleCommonException(CafeCommonException ex, HttpServletRequest request) {
        CommonResponse commonResponse = new CommonResponse(CommonResponse.Meta.builder().code(400).message(ex.getMessage()).build()
                , request.getServletPath());
        logger.error(commonResponse.toString());
        return ResponseEntity.badRequest().body(commonResponse);
    }

    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public ResponseEntity<Object> handleValidationException(MethodArgumentNotValidException ex, HttpServletRequest request) {
        List<String> errorMessages = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());
        CommonResponse commonResponse = new CommonResponse(CommonResponse.Meta.builder().code(400).message(String.join(", ", errorMessages)).build()
                , request.getServletPath());
        logger.error(commonResponse.toString());
        return ResponseEntity.badRequest().body(commonResponse);
    }
}
