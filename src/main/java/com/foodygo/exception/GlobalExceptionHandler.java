package com.foodygo.exception;

import com.foodygo.dto.response.ErrorResponse;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> unauthorizedException(AuthenticationException e, WebRequest request) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse(new Date(), "Thất bại", "Bạn không có quyền truy cập", e.getMessage(), request.getDescription(false).replace("uri=", "")));
    }

    @ExceptionHandler(ElementNotFoundException.class)
    public ResponseEntity<ErrorResponse> elementNotFoundException(ElementNotFoundException e, WebRequest request) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(new Date(), "Thất bại", "Không tìm thấy", e.getMessage(), request.getDescription(false).replace("uri=", "")));
    }

    @ExceptionHandler(ElementExistException.class)
    public ResponseEntity<ErrorResponse> elementExistException(ElementExistException e, WebRequest request) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(new Date(), "Thất bại", "Phần tử đã tồn tại", e.getMessage(), request.getDescription(false).replace("uri=", "")));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> elementExistException(AccessDeniedException e, WebRequest request) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ErrorResponse(new Date(), "Thất bại", "Bạn không có quyền truy cập API này", e.getMessage(), request.getDescription(false).replace("uri=", "")));
    }

    @ExceptionHandler(UnchangedStateException.class)
    public ResponseEntity<ErrorResponse> unchangedStateException(UnchangedStateException e, WebRequest request) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(new Date(), "Thất bại", "Phần tử không thay đổi", e.getMessage(), request.getDescription(false).replace("uri=", "")));
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErrorResponse> noResourceFoundException(NoResourceFoundException e, WebRequest request) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(new Date(), "Thất bại", "Không tìm thấy tài nguyên", e.getMessage(), request.getDescription(false).replace("uri=", "")));
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<ErrorResponse> unauthorizedExceptionJWT(ExpiredJwtException e, WebRequest request) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse(new Date(), "Thất bại", "Bạn không có quyền truy cập", e.getMessage(), request.getDescription(false).replace("uri=", "")));
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleException(MethodArgumentNotValidException ex, WebRequest request) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(new Date(), "Thất bại", "Một số tham số không hợp lệ", errors, request.getDescription(false).replace("uri=", "")));
    }

    @ExceptionHandler(IdNotFoundException.class)
    public ResponseEntity<ErrorResponse> idNotFoundException(IdNotFoundException e, WebRequest request) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(new Date(), "Thất bại", "Không tìm thấy ID", e.getMessage(), request.getDescription(false).replace("uri=", "")));
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> badRequestException(BadRequestException e, WebRequest request) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(new Date(), "Thất bại", "Yêu cầu không hợp lệ", e.getMessage(), request.getDescription(false).replace("uri=", "")));
    }

}