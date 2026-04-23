package com.productpool.backend.exception;

/**
 * 资源未找到异常
 * 当查询的资源在数据库中不存在时抛出此异常
 */
public class ResourceNotFoundException extends RuntimeException {

  /** 根据自定义消息构造异常 */
  public ResourceNotFoundException(String message) {
    super(message);
  }

  /** 根据资源名称和ID构造异常，生成标准错误信息 */
  public ResourceNotFoundException(String resourceName, Object id) {
    super(String.format("%s not found with id: %s", resourceName, id));
  }
}
