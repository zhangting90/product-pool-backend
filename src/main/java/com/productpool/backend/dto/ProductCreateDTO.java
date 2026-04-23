package com.productpool.backend.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** 产品创建请求DTO 用于接收前端创建产品时的请求参数 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductCreateDTO {

  /** 产品名称 */
  @NotBlank(message = "Name is required")
  @Size(max = 200, message = "Name must not exceed 200 characters")
  private String name;

  /** 产品编码 */
  @NotBlank(message = "Code is required")
  @Size(max = 50, message = "Code must not exceed 50 characters")
  private String code;

  /** 所属策略类型ID */
  @NotNull(message = "Strategy type ID is required")
  private Long strategyTypeId;

  /** 风险等级 */
  @NotBlank(message = "Risk level is required")
  @Size(max = 10, message = "Risk level must not exceed 10 characters")
  private String riskLevel;

  /** 年化收益率（%） */
  @DecimalMin(value = "-100", message = "Annual return must be greater than or equal to -100")
  @DecimalMax(value = "1000", message = "Annual return must be less than or equal to 1000")
  private BigDecimal annualReturn;

  /** 波动率（%） */
  @DecimalMin(value = "0", message = "Volatility must be greater than or equal to 0")
  private BigDecimal volatility;

  /** 夏普比率 */
  @DecimalMin(value = "-10", message = "Sharpe ratio must be greater than or equal to -10")
  @DecimalMax(value = "10", message = "Sharpe ratio must be less than or equal to 10")
  private BigDecimal sharpeRatio;

  /** 最大回撤（%） */
  @DecimalMin(value = "-100", message = "Max drawdown must be greater than or equal to -100")
  @DecimalMax(value = "0", message = "Max drawdown must be less than or equal to 0")
  private BigDecimal maxDrawdown;

  /** 基金经理 */
  @Size(max = 100, message = "Fund manager must not exceed 100 characters")
  private String fundManager;

  /** 基金规模（亿元） */
  @DecimalMin(value = "0", message = "Fund scale must be greater than or equal to 0")
  private BigDecimal fundScale;

  /** 成立日期 */
  private LocalDateTime inceptionDate;

  /** 产品描述 */
  @Size(max = 1000, message = "Description must not exceed 1000 characters")
  private String description;

  /** 是否启用 */
  private Boolean isActive;

  /** 排序序号 */
  private Integer sortOrder;
}
