package com.productpool.backend.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductUpdateDTO {

  @NotBlank(message = "Name is required")
  @Size(max = 200, message = "Name must not exceed 200 characters")
  private String name;

  @Size(max = 1000, message = "Description must not exceed 1000 characters")
  private String description;

  @NotBlank(message = "Risk level is required")
  @Size(max = 10, message = "Risk level must not exceed 10 characters")
  private String riskLevel;

  @DecimalMin(value = "-100", message = "Annual return must be greater than or equal to -100")
  @DecimalMax(value = "1000", message = "Annual return must be less than or equal to 1000")
  private BigDecimal annualReturn;

  @DecimalMin(value = "0", message = "Volatility must be greater than or equal to 0")
  private BigDecimal volatility;

  @DecimalMin(value = "-10", message = "Sharpe ratio must be greater than or equal to -10")
  @DecimalMax(value = "10", message = "Sharpe ratio must be less than or equal to 10")
  private BigDecimal sharpeRatio;

  @DecimalMin(value = "-100", message = "Max drawdown must be greater than or equal to -100")
  @DecimalMax(value = "0", message = "Max drawdown must be less than or equal to 0")
  private BigDecimal maxDrawdown;

  @Size(max = 100, message = "Fund manager must not exceed 100 characters")
  private String fundManager;

  @DecimalMin(value = "0", message = "Fund scale must be greater than or equal to 0")
  private BigDecimal fundScale;

  private LocalDateTime inceptionDate;

  private Boolean isActive;

  private Integer sortOrder;
}
