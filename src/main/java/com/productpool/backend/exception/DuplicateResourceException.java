package com.productpool.backend.exception;

/** 资源重复异常 当尝试创建的资源在数据库中已存在（如编码重复）时抛出此异常 */
public class DuplicateResourceException extends RuntimeException {

  /** 根据自定义消息构造异常 */
  public DuplicateResourceException(String message) {
    super(message);
  }

  /** 根据资源名称、字段名和字段值构造异常，生成标准错误信息 */
  public DuplicateResourceException(String resourceName, String fieldName, Object fieldValue) {
    super(String.format("%s already exists with %s: %s", resourceName, fieldName, fieldValue));
  }
}
