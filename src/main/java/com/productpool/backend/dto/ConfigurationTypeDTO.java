package com.productpool.backend.dto;

import com.productpool.backend.entity.ConfigurationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConfigurationTypeDTO {

  private Long id;

  private String name;

  private String code;

  private String description;

  private Boolean isMajor;

  private Long parentId;

  private Integer sortOrder;

  private LocalDateTime createdAt;

  private LocalDateTime updatedAt;

  private List<BenchmarkDTO> benchmarks;

  private List<ConfigurationTypeDTO> children;

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
