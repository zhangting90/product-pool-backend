package com.productpool.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** 策略类型查询请求DTO 用于接收前端查询策略类型列表时的筛选条件 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StrategyTypeQueryDTO {

  /** 策略类型ID */
  private Long id;

  /** 策略类型名称（模糊查询） */
  private String name;

  /** 所属基准指标ID */
  private Long benchmarkId;

  /** 配置类型大分类ID（可选，用于多级筛选） */
  private Long majorTypeId;

  /** 配置类型子分类ID（可选，用于多级筛选） */
  private Long subTypeId;
}
