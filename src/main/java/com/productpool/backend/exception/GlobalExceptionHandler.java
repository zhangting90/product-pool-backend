package com.productpool.backend.exception;

import com.productpool.backend.dto.Result;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

/** 全局异常处理器 统一捕获和处理所有 Controller 层抛出的异常，返回标准格式的错误响应 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  /** 处理资源未找到异常，返回 404 */
  @ExceptionHandler(ResourceNotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public Result<Void> handleResourceNotFoundException(ResourceNotFoundException ex) {
    log.warn("Resource not found: {}", ex.getMessage());
    return Result.error(404, ex.getMessage());
  }

  /** 处理资源重复异常，返回 409 */
  @ExceptionHandler(DuplicateResourceException.class)
  @ResponseStatus(HttpStatus.CONFLICT)
  public Result<Void> handleDuplicateResourceException(DuplicateResourceException ex) {
    log.warn("Duplicate resource: {}", ex.getMessage());
    return Result.error(409, ex.getMessage());
  }

  /** 处理业务逻辑异常，返回 400 */
  @ExceptionHandler(BusinessLogicException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public Result<Void> handleBusinessLogicException(BusinessLogicException ex) {
    log.warn("Business logic error: {}", ex.getMessage());
    return Result.error(400, ex.getMessage());
  }

  /** 处理请求参数校验异常（@Valid 触发），收集所有字段错误信息 */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public Result<Map<String, String>> handleMethodArgumentNotValidException(
      MethodArgumentNotValidException ex) {
    log.warn("Validation error: {}", ex.getMessage());
    Map<String, String> errors = new HashMap<>();
    ex.getBindingResult()
        .getAllErrors()
        .forEach(
            error -> {
              String fieldName = ((FieldError) error).getField();
              String errorMessage = error.getDefaultMessage();
              errors.put(fieldName, errorMessage);
            });
    return Result.error(400, "Validation failed");
  }

  /** 处理约束违反异常（如 @RequestParam 校验失败），收集违规字段信息 */
  @ExceptionHandler(ConstraintViolationException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public Result<Map<String, String>> handleConstraintViolationException(
      ConstraintViolationException ex) {
    log.warn("Constraint violation: {}", ex.getMessage());
    Map<String, String> errors =
        ex.getConstraintViolations().stream()
            .collect(
                Collectors.toMap(
                    violation -> violation.getPropertyPath().toString(),
                    ConstraintViolation::getMessage,
                    (existing, replacement) -> existing));
    return Result.error(400, "Validation failed");
  }

  /** 处理缺少请求参数异常 */
  @ExceptionHandler(MissingServletRequestParameterException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public Result<Void> handleMissingServletRequestParameterException(
      MissingServletRequestParameterException ex) {
    log.warn("Missing request parameter: {}", ex.getMessage());
    return Result.error(
        400, String.format("Missing required parameter: %s", ex.getParameterName()));
  }

  /** 处理HTTP请求方法不支持异常，返回 405 */
  @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
  @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
  public Result<Void> handleHttpRequestMethodNotSupportedException(
      HttpRequestMethodNotSupportedException ex) {
    log.warn("Method not supported: {}", ex.getMessage());
    return Result.error(405, String.format("Method not supported: %s", ex.getMethod()));
  }

  /** 处理请求体解析异常（JSON格式错误等） */
  @ExceptionHandler(HttpMessageNotReadableException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public Result<Void> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
    log.warn("Message not readable: {}", ex.getMessage());
    return Result.error(400, "Invalid request body");
  }

  /** 处理参数类型不匹配异常（如传入字符串但期望数字） */
  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public Result<Void> handleMethodArgumentTypeMismatchException(
      MethodArgumentTypeMismatchException ex) {
    log.warn("Type mismatch: {}", ex.getMessage());
    return Result.error(400, String.format("Invalid parameter type for '%s'", ex.getName()));
  }

  /** 处理数据完整性违反异常（唯一约束冲突、外键约束违反等） */
  @ExceptionHandler(DataIntegrityViolationException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public Result<Void> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
    log.warn("Data integrity violation: {}", ex.getMessage());
    String message = "Data integrity violation";
    if (ex.getMostSpecificCause() != null) {
      String causeMessage = ex.getMostSpecificCause().getMessage();
      if (causeMessage != null) {
        if (causeMessage.contains("unique constraint") || causeMessage.contains("duplicate key")) {
          message = "Duplicate entry detected";
        } else if (causeMessage.contains("foreign key constraint")) {
          message = "Cannot delete or update due to existing references";
        }
      }
    }
    return Result.error(400, message);
  }

  /** 处理未找到请求处理器异常（接口不存在），返回 404 */
  @ExceptionHandler(NoHandlerFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public Result<Void> handleNoHandlerFoundException(NoHandlerFoundException ex) {
    log.warn("No handler found: {}", ex.getMessage());
    return Result.error(404, "Endpoint not found");
  }

  /** 处理非法参数异常 */
  @ExceptionHandler(IllegalArgumentException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public Result<Void> handleIllegalArgumentException(IllegalArgumentException ex) {
    log.warn("Illegal argument: {}", ex.getMessage());
    return Result.error(400, ex.getMessage());
  }

  /** 处理所有未捕获的异常，作为兜底返回 500 */
  @ExceptionHandler(Exception.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public Result<Void> handleGenericException(Exception ex) {
    log.error("Unexpected error occurred", ex);
    return Result.error(500, "Internal server error");
  }
}
