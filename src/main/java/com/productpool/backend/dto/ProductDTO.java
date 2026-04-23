package com.productpool.backend.dto;

import com.productpool.backend.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {

  private Long id;

  private String name;

  private String code;

  private Long strategyTypeId;

  private String strategyTypeName;

  private String riskLevel;

  private BigDecimal annualReturn;

  private BigDecimal volatility;

  private BigDecimal sharpeRatio;

  private BigDecimal maxDrawdown;

  private String fundManager;

  private BigDecimal fundScale;

  private LocalDateTime inceptionDate;

  private String description;

  private Boolean isActive;

  private Integer sortOrder;

  private LocalDateTime createdAt;

  private LocalDateTime updatedAt;

  public static ProductDTO fromEntity(Product entity) {
    ProductDTO dto = new ProductDTO();
    dto.setId(entity.getId());
    dto.setName(entity.getName());
    dto.setCode(entity.getCode());
    if (entity.getStrategyType() != null) {
      dto.setStrategyTypeId(entity.getStrategyType().getId());
      dto.setStrategyTypeName(entity.getStrategyType().getName());
    }
    dto.setRiskLevel(entity.getRiskLevel());
    dto.setAnnualReturn(entity.getAnnualReturn());
    dto.setVolatility(entity.getVolatility());
    dto.setSharpeRatio(entity.getSharpeRatio());
    dto.setMaxDrawdown(entity.getMaxDrawdown());
    dto.setFundManager(entity.getFundManager());
    dto.setFundScale(entity.getFundScale());
    dto.setInceptionDate(entity.getInceptionDate());
    dto.setDescription(entity.getDescription());
    dto.setIsActive(entity.getIsActive());
    dto.setSortOrder(entity.getSortOrder());
    dto.setCreatedAt(entity.getCreatedAt());
    dto.setUpdatedAt(entity.getUpdatedAt());
    return dto;
  }
}
