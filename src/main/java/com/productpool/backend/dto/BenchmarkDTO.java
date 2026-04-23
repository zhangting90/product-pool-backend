package com.productpool.backend.dto;

import com.productpool.backend.entity.Benchmark;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 基准指标响应DTO
 * 用于返回基准指标的完整信息，包含关联的策略类型列表
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BenchmarkDTO {

  /** 基准指标ID */
  private Long id;

  /** 基准指标名称 */
  private String name;

  /** 基准指标编码 */
  private String code;

  /** 基准指标描述 */
  private String description;

  /** 所属配置类型ID */
  private Long configurationTypeId;

  /** 所属配置类型名称 */
  private String configurationTypeName;

  /** 排序序号 */
  private Integer sortOrder;

  /** 创建时间 */
  private LocalDateTime createdAt;

  /** 更新时间 */
  private LocalDateTime updatedAt;

  /** 关联的策略类型列表 */
  private List<StrategyTypeDTO> strategyTypes;

  /**
   * 从实体对象转换为DTO
   *
   * @param entity 基准指标实体
   * @return 基准指标DTO
   */
  public static BenchmarkDTO fromEntity(Benchmark entity) {
    BenchmarkDTO dto = new BenchmarkDTO();
    dto.setId(entity.getId());
    dto.setName(entity.getName());
    dto.setCode(entity.getCode());
    dto.setDescription(entity.getDescription());
    dto.setConfigurationTypeId(entity.getConfigurationTypeId());
    dto.setSortOrder(entity.getSortOrder());
    dto.setCreatedAt(entity.getCreatedAt());
    dto.setUpdatedAt(entity.getUpdatedAt());
    return dto;
  }
}
