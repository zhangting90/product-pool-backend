package com.productpool.backend.exception;

/**
 * 业务逻辑异常
 * 当业务规则校验不通过时抛出此异常，如删除存在依赖的数据
 */
public class BusinessLogicException extends RuntimeException {

  /** 根据错误消息构造异常 */
  public BusinessLogicException(String message) {
    super(message);
  }

  /** 根据错误消息和原因构造异常，支持异常链 */
  public BusinessLogicException(String message, Throwable cause) {
    super(message, cause);
  }
}
