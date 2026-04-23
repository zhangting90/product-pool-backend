package com.productpool.backend.dto;

import com.productpool.backend.entity.ConfigurationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 配置类型响应DTO
 * 用于返回配置类型的完整信息，包含关联的基准指标和子类型列表
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConfigurationTypeDTO {

  /** 配置类型ID */
  private Long id;

  /** 配置类型名称 */
  private String name;

  /** 配置类型编码 */
  private String code;

  /** 配置类型描述 */
  private String description;

  /** 是否为主类型 */
  private Boolean isMajor;

  /** 父级配置类型ID */
  private Long parentId;

  /** 排序序号 */
  private Integer sortOrder;

  /** 创建时间 */
  private LocalDateTime createdAt;

  /** 更新时间 */
  private LocalDateTime updatedAt;

  /** 关联的基准指标列表 */
  private List<BenchmarkDTO> benchmarks;

  /** 子配置类型列表 */
  private List<ConfigurationTypeDTO> children;

  /**
   * 从实体对象转换为DTO
   *
   * @param entity 配置类型实体
   * @return 配置类型DTO
   */
  public static ConfigurationTypeDTO fromEntity(ConfigurationType entity) {
    ConfigurationTypeDTO dto = new ConfigurationTypeDTO();
    dto.setId(entity.getId());
    dto.setName(entity.getName());
    dto.setCode(entity.getCode());
    dto.setDescription(entity.getDescription());
    dto.setIsMajor(entity.getIsMajor());
    dto.setParentId(entity.getParentId());
    dto.setSortOrder(entity.getSortOrder());
    dto.setCreatedAt(entity.getCreatedAt());
    dto.setUpdatedAt(entity.getUpdatedAt());
    return dto;
  }
}
