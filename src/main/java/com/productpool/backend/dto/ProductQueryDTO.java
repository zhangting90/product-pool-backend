package com.productpool.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** 产品查询请求DTO 用于接收前端查询产品列表时的筛选条件 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductQueryDTO {

  /** 产品名称（模糊查询） */
  private String name;

  /** 产品编码（模糊查询） */
  private String code;

  /** 所属策略类型ID */
  private Long strategyTypeId;
}
