package com.productpool.backend.dto;

import com.productpool.backend.entity.Benchmark;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BenchmarkDTO {

  private Long id;

  private String name;

  private String code;

  private String description;

  private Long configurationTypeId;

  private String configurationTypeName;

  private Integer sortOrder;

  private LocalDateTime createdAt;

  private LocalDateTime updatedAt;

  private List<StrategyTypeDTO> strategyTypes;

  public static BenchmarkDTO fromEntity(Benchmark entity) {
    BenchmarkDTO dto = new BenchmarkDTO();
    dto.setId(entity.getId());
    dto.setName(entity.getName());
    dto.setCode(entity.getCode());
    dto.setDescription(entity.getDescription());
    if (entity.getConfigurationType() != null) {
      dto.setConfigurationTypeId(entity.getConfigurationType().getId());
      dto.setConfigurationTypeName(entity.getConfigurationType().getName());
    }
    dto.setSortOrder(entity.getSortOrder());
    dto.setCreatedAt(entity.getCreatedAt());
    dto.setUpdatedAt(entity.getUpdatedAt());
    return dto;
  }
}
