package com.productpool.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** 基准指标查询请求DTO 用于接收前端查询基准指标列表时的筛选条件 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BenchmarkQueryDTO {

  /** 基准指标ID */
  private Long id;

  /** 基准指标名称（模糊查询） */
  private String name;

  /** 基准指标编码（模糊查询） */
  private String code;

  /** 所属配置类型ID */
  private Long configurationTypeId;
}
