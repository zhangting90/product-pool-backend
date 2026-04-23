package com.productpool.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 统一响应体类
 * 封装所有API接口的返回结果，包含状态码、消息、数据和时间戳
 *
 * @param <T> 响应数据的泛型类型
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result<T> implements Serializable {

  private static final long serialVersionUID = 1L;

  /** 响应状态码，200 表示成功 */
  private Integer code;

  /** 响应消息描述 */
  private String message;

  /** 响应数据 */
  private T data;

  /** 响应时间戳 */
  private Long timestamp;

  /** 返回成功结果（无数据） */
  public static <T> Result<T> success() {
    return success(null);
  }

  /** 返回成功结果（带数据） */
  public static <T> Result<T> success(T data) {
    return new Result<>(200, "success", data, System.currentTimeMillis());
  }

  /** 返回成功结果（带自定义消息和数据） */
  public static <T> Result<T> success(String message, T data) {
    return new Result<>(200, message, data, System.currentTimeMillis());
  }

  /** 返回错误结果（带状态码和消息） */
  public static <T> Result<T> error(Integer code, String message) {
    return new Result<>(code, message, null, System.currentTimeMillis());
  }

  /** 返回错误结果（默认500状态码） */
  public static <T> Result<T> error(String message) {
    return error(500, message);
  }
}
