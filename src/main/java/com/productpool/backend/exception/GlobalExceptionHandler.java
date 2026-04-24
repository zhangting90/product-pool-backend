package com.productpool.backend.exception;

import com.productpool.backend.dto.Result;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

/**
 * 全局异常处理器
 *
 * <p>统一捕获和处理所有 Controller 层抛出的异常，始终返回 HTTP 200 + Result 错误格式， 由前端通过 Result.code 判断业务是否成功。
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  /** 处理资源未找到异常 */
  @ExceptionHandler(ResourceNotFoundException.class)
  public Result<Void> handleResourceNotFoundException(ResourceNotFoundException ex) {
    log.warn("资源未找到: {}", ex.getMessage());
    return Result.error(404, ex.getMessage());
  }

  /** 处理资源重复异常 */
  @ExceptionHandler(DuplicateResourceException.class)
  public Result<Void> handleDuplicateResourceException(DuplicateResourceException ex) {
    log.warn("资源重复: {}", ex.getMessage());
    return Result.error(409, ex.getMessage());
  }

  /** 处理业务逻辑异常 */
  @ExceptionHandler(BusinessLogicException.class)
  public Result<Void> handleBusinessLogicException(BusinessLogicException ex) {
    log.warn("业务逻辑错误: {}", ex.getMessage());
    return Result.error(400, ex.getMessage());
  }

  /** 处理请求参数校验异常（@Valid 触发），收集所有字段错误信息 */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public Result<Map<String, String>> handleMethodArgumentNotValidException(
      MethodArgumentNotValidException ex) {
    log.warn("参数校验失败: {}", ex.getMessage());
    Map<String, String> errors = new HashMap<>();
    ex.getBindingResult()
        .getAllErrors()
        .forEach(
            error -> {
              String fieldName = ((FieldError) error).getField();
              String errorMessage = error.getDefaultMessage();
              errors.put(fieldName, errorMessage);
            });
    return Result.error(400, "参数校验失败");
  }

  /** 处理约束违反异常（如 @RequestParam 校验失败），收集违规字段信息 */
  @ExceptionHandler(ConstraintViolationException.class)
  public Result<Map<String, String>> handleConstraintViolationException(
      ConstraintViolationException ex) {
    log.warn("约束违反: {}", ex.getMessage());
    Map<String, String> errors =
        ex.getConstraintViolations().stream()
            .collect(
                Collectors.toMap(
                    violation -> violation.getPropertyPath().toString(),
                    ConstraintViolation::getMessage,
                    (existing, replacement) -> existing));
    return Result.error(400, "参数校验失败");
  }

  /** 处理缺少请求参数异常 */
  @ExceptionHandler(MissingServletRequestParameterException.class)
  public Result<Void> handleMissingServletRequestParameterException(
      MissingServletRequestParameterException ex) {
    log.warn("缺少请求参数: {}", ex.getMessage());
    return Result.error(400, String.format("缺少必要参数：%s", ex.getParameterName()));
  }

  /** 处理HTTP请求方法不支持异常 */
  @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
  public Result<Void> handleHttpRequestMethodNotSupportedException(
      HttpRequestMethodNotSupportedException ex) {
    log.warn("请求方法不支持: {}", ex.getMessage());
    return Result.error(405, String.format("不支持的请求方法：%s", ex.getMethod()));
  }

  /** 处理请求体解析异常（JSON格式错误等） */
  @ExceptionHandler(HttpMessageNotReadableException.class)
  public Result<Void> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
    log.warn("请求体解析失败: {}", ex.getMessage());
    return Result.error(400, "请求体格式错误，请检查JSON格式");
  }

  /** 处理参数类型不匹配异常（如传入字符串但期望数字） */
  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  public Result<Void> handleMethodArgumentTypeMismatchException(
      MethodArgumentTypeMismatchException ex) {
    log.warn("参数类型不匹配: {}", ex.getMessage());
    return Result.error(400, String.format("参数 '%s' 类型不正确", ex.getName()));
  }

  /** 处理数据完整性违反异常（唯一约束冲突、外键约束违反等） */
  @ExceptionHandler(DataIntegrityViolationException.class)
  public Result<Void> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
    log.warn("数据完整性错误: {}", ex.getMessage());
    String message = "数据完整性约束违反";
    if (ex.getMostSpecificCause() != null) {
      String causeMessage = ex.getMostSpecificCause().getMessage();
      if (causeMessage != null) {
        if (causeMessage.contains("unique constraint") || causeMessage.contains("duplicate key")) {
          message = "数据重复，唯一约束冲突";
        } else if (causeMessage.contains("foreign key constraint")) {
          message = "无法删除或更新，存在关联数据引用";
        }
      }
    }
    return Result.error(400, message);
  }

  /** 处理未找到请求处理器异常（接口不存在） */
  @ExceptionHandler(NoHandlerFoundException.class)
  public Result<Void> handleNoHandlerFoundException(NoHandlerFoundException ex) {
    log.warn("接口不存在: {}", ex.getMessage());
    return Result.error(404, "请求的接口不存在");
  }

  /** 处理非法参数异常 */
  @ExceptionHandler(IllegalArgumentException.class)
  public Result<Void> handleIllegalArgumentException(IllegalArgumentException ex) {
    log.warn("非法参数: {}", ex.getMessage());
    return Result.error(400, ex.getMessage());
  }

  /** 处理所有未捕获的异常，作为兜底 */
  @ExceptionHandler(Exception.class)
  public Result<Void> handleGenericException(Exception ex) {
    log.error("服务器内部错误", ex);
    return Result.error(500, "服务器内部错误");
  }
}
