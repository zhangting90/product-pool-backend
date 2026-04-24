package com.productpool.backend.dto;

import java.util.List;
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

  /** 配置分类ID（支持直接按分类筛选） */
  private Long configurationTypeId;

  /** 业绩对标ID（支持直接按业绩对标筛选） */
  private Long benchmarkId;

  /** 所属策略类型ID（单个） */
  private Long strategyTypeId;

  /** 所属策略类型ID列表（批量，支持多级筛选） */
  private List<Long> strategyTypeIds;
}
