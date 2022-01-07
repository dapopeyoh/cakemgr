package com.waracle.cakemgr.exception.handler;

import com.waracle.cakemgr.dto.ApiResponse;
import com.waracle.cakemgr.exception.CakeNotFoundException;
import com.waracle.cakemgr.exception.InvalidCakeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

  @ExceptionHandler(CakeNotFoundException.class)
  public final ResponseEntity<ApiResponse<?>> handleCakeNotFoundException(CakeNotFoundException ex) {
    ApiResponse<?> apiResponse = ApiResponse.builder()
            .message(ex.getMessage())
            .build();
    return ResponseEntity.badRequest().body(apiResponse);
  }

  @ExceptionHandler(InvalidCakeException.class)
  public final ResponseEntity<ApiResponse<?>> handleInvalidCakeException(InvalidCakeException ex) {
    ApiResponse<?> apiResponse = ApiResponse.builder()
            .message(ex.getMessage())
            .build();

    return ResponseEntity.badRequest().body(apiResponse);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public final ResponseEntity<ApiResponse<?>> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
    final List<String> errors = new ArrayList<>();
    for (final FieldError error : ex.getBindingResult().getFieldErrors()) {
      errors.add(error.getField() + ": " + error.getDefaultMessage());
    }
    for (final ObjectError error : ex.getBindingResult().getGlobalErrors()) {
      errors.add(error.getObjectName() + ": " + error.getDefaultMessage());
    }

    ApiResponse<?> apiResponse = ApiResponse.builder()
            .message("Validation Failed")
            .data(errors)
            .build();

    return ResponseEntity.badRequest().body(apiResponse);
  }

  @ExceptionHandler(Exception.class)
  public final ResponseEntity<ApiResponse<?>> handleOtherExceptions(Exception ex) {
    log.info("Unhandled exception occurred: ", ex);
    ApiResponse<?> apiResponse = ApiResponse.builder()
            .message(ex.getMessage())
            .build();
    return ResponseEntity.badRequest().body(apiResponse);
  }
}
