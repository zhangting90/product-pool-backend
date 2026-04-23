package com.productpool.backend.entity;

import com.productpool.backend.util.IdGenerator;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "product", uniqueConstraints = {
  @UniqueConstraint(name = "uk_product_code", columnNames = "code")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {

  @Id
  @Column(name = "id", nullable = false, updatable = false)
  private Long id;

  @Column(name = "name", nullable = false, length = 200)
  private String name;

  @Column(name = "code", nullable = false, unique = true, length = 50)
  private String code;

  @Column(name = "strategy_type_id", nullable = false)
  private Long strategyTypeId;

  @Column(name = "risk_level", nullable = false, length = 10)
  private String riskLevel;

  @Column(name = "annual_return", precision = 10, scale = 4)
  private BigDecimal annualReturn;

  @Column(name = "volatility", precision = 10, scale = 4)
  private BigDecimal volatility;

  @Column(name = "sharpe_ratio", precision = 10, scale = 4)
  private BigDecimal sharpeRatio;

  @Column(name = "max_drawdown", precision = 10, scale = 4)
  private BigDecimal maxDrawdown;

  @Column(name = "fund_manager", length = 100)
  private String fundManager;

  @Column(name = "fund_scale", precision = 20, scale = 2)
  private BigDecimal fundScale;

  @Column(name = "inception_date")
  private LocalDateTime inceptionDate;

  @Column(name = "description", length = 1000)
  private String description;

  @Column(name = "is_active", nullable = false)
  private Boolean isActive;

  @Column(name = "sort_order")
  private Integer sortOrder;

  @Column(name = "created_at", nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @Column(name = "updated_at")
  private LocalDateTime updatedAt;

  @PrePersist
  protected void onCreate() {
    if (id == null) {
      id = IdGenerator.generateId();
    }
    createdAt = LocalDateTime.now();
    updatedAt = LocalDateTime.now();
    if (isActive == null) {
      isActive = true;
    }
    if (sortOrder == null) {
      sortOrder = 0;
    }
  }

  @PreUpdate
  protected void onUpdate() {
    updatedAt = LocalDateTime.now();
  }
}
