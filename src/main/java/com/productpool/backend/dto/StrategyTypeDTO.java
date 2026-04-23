package com.productpool.backend.dto;

import com.productpool.backend.entity.StrategyType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StrategyTypeDTO {

  private Long id;

  private String name;

  private String description;

  private Long benchmarkId;

  private String benchmarkName;

  private Integer sortOrder;

  private LocalDateTime createdAt;

  private LocalDateTime updatedAt;

  private List<ProductDTO> products;

  public static StrategyTypeDTO fromEntity(StrategyType entity) {
    StrategyTypeDTO dto = new StrategyTypeDTO();
    dto.setId(entity.getId());
    dto.setName(entity.getName());
    dto.setDescription(entity.getDescription());
    if (entity.getBenchmark() != null) {
      dto.setBenchmarkId(entity.getBenchmark().getId());
      dto.setBenchmarkName(entity.getBenchmark().getName());
    }
    dto.setSortOrder(entity.getSortOrder());
    dto.setCreatedAt(entity.getCreatedAt());
    dto.setUpdatedAt(entity.getUpdatedAt());
    return dto;
  }
}
