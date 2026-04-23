package com.productpool.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 分页结果通用DTO
 * 用于封装分页查询的返回结果，包含数据列表和分页信息
 *
 * @param <T> 数据类型
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageResult<T> {

  /** 当前页数据列表 */
  private List<T> content;

  /** 当前页码（从0开始） */
  private int pageNumber;

  /** 每页大小 */
  private int pageSize;

  /** 总记录数 */
  private long totalElements;

  /** 总页数 */
  private int totalPages;

  /** 是否为首页 */
  private boolean first;

  /** 是否为末页 */
  private boolean last;
}
