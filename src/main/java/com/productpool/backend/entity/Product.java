package com.productpool.backend.entity;

import com.productpool.backend.util.IdGenerator;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** 产品实体类 对应数据库表 product，用于管理产品信息 关联到策略类型，一个策略类型下可有多个产品 包含产品的业绩指标（年化收益、波动率、夏普比率、最大回撤等） */
@Entity
@Table(
    name = "product",
    uniqueConstraints = {@UniqueConstraint(name = "uk_product_code", columnNames = "code")})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {

  /** 主键ID */
  @Id
  @Column(name = "id", nullable = false, updatable = false)
  private Long id;

  /** 产品名称 */
  @Column(name = "name", nullable = false, length = 200)
  private String name;

  /** 产品编码，全局唯一 */
  @Column(name = "code", nullable = false, unique = true, length = 50)
  private String code;

  /** 所属策略类型ID，关联 strategy_type 表 */
  @Column(name = "strategy_type_id", nullable = false)
  private Long strategyTypeId;

  /** 风险等级，如 R1~R5 */
  @Column(name = "risk_level", nullable = false, length = 10)
  private String riskLevel;

  /** 年化收益率 */
  @Column(name = "annual_return", precision = 10, scale = 4)
  private BigDecimal annualReturn;

  /** 波动率 */
  @Column(name = "volatility", precision = 10, scale = 4)
  private BigDecimal volatility;

  /** 夏普比率 */
  @Column(name = "sharpe_ratio", precision = 10, scale = 4)
  private BigDecimal sharpeRatio;

  /** 最大回撤 */
  @Column(name = "max_drawdown", precision = 10, scale = 4)
  private BigDecimal maxDrawdown;

  /** 基金经理 */
  @Column(name = "fund_manager", length = 100)
  private String fundManager;

  /** 基金规模 */
  @Column(name = "fund_scale", precision = 20, scale = 2)
  private BigDecimal fundScale;

  /** 成立日期 */
  @Column(name = "inception_date")
  private LocalDateTime inceptionDate;

  /** 产品描述 */
  @Column(name = "description", length = 1000)
  private String description;

  /** 是否启用，true 表示产品在用 */
  @Column(name = "is_active", nullable = false)
  private Boolean isActive;

  /** 排序序号 */
  @Column(name = "sort_order")
  private Integer sortOrder;

  /** 创建时间 */
  @Column(name = "created_at", nullable = false, updatable = false)
  private LocalDateTime createdAt;

  /** 更新时间 */
  @Column(name = "updated_at")
  private LocalDateTime updatedAt;

  /** 持久化前回调：自动生成ID、设置时间戳和默认值 */
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

  /** 更新前回调：自动刷新更新时间 */
  @PreUpdate
  protected void onUpdate() {
    updatedAt = LocalDateTime.now();
  }
}
