package com.productpool.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result<T> implements Serializable {

  private static final long serialVersionUID = 1L;

  private Integer code;

  private String message;

  private T data;

  private Long timestamp;

  public static <T> Result<T> success() {
    return success(null);
  }

  public static <T> Result<T> success(T data) {
    return new Result<>(200, "success", data, System.currentTimeMillis());
  }

  public static <T> Result<T> success(String message, T data) {
    return new Result<>(200, message, data, System.currentTimeMillis());
  }

  public static <T> Result<T> error(Integer code, String message) {
    return new Result<>(code, message, null, System.currentTimeMillis());
  }

  public static <T> Result<T> error(String message) {
    return error(500, message);
  }
}
