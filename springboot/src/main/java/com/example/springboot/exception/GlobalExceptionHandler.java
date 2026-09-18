package com.example.springboot.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.LinkedHashMap;
import java.util.Map;

// @RestControllerAdvice = @ControllerAdvice + @ResponseBody
// ControllerAdvice 本质就是一个"全局切面"：所有 Controller 抛异常时，都由它统一接管
@RestControllerAdvice
public class GlobalExceptionHandler {

    // ① 参数校验失败：@Valid 在"进方法体之前"就抛出来了，这是最常被问到的一个
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValid(MethodArgumentNotValidException e) {
        Map<String, String> errors = new LinkedHashMap<>();
        for (FieldError fe : e.getBindingResult().getFieldErrors()) {
            errors.put(fe.getField(), fe.getDefaultMessage());
        }
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("code", 400);
        body.put("msg", "参数校验不通过");
        body.put("errors", errors);   // 告诉前端：到底哪个字段错了、错在哪
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    // ② 业务自己抛的"查不到" → 翻译成 404
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNotFound(UserNotFoundException e) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("code", 404);
        body.put("msg", e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }

    // ③ 兜底：任何没被上面接住的异常，都别把 500 堆栈甩给前端
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleOther(Exception e) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("code", 500);
        body.put("msg", "服务器内部错误");
        body.put("detail", e.getClass().getSimpleName());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }
}
