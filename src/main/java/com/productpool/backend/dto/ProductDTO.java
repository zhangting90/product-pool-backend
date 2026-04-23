package com.productpool.backend.dto;

import com.productpool.backend.entity.Product;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** 产品响应DTO 用于返回产品的完整信息 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {

  /** 产品ID */
  private Long id;

  /** 产品名称 */
  private String name;

  /** 产品编码 */
  private String code;

  /** 所属策略类型ID */
  private Long strategyTypeId;

  /** 所属策略类型名称 */
  private String strategyTypeName;

  /** 风险等级 */
  private String riskLevel;

  /** 年化收益率（%） */
  private BigDecimal annualReturn;

  /** 波动率（%） */
  private BigDecimal volatility;

  /** 夏普比率 */
  private BigDecimal sharpeRatio;

  /** 最大回撤（%） */
  private BigDecimal maxDrawdown;

  /** 基金经理 */
  private String fundManager;

  /** 基金规模（亿元） */
  private BigDecimal fundScale;

  /** 成立日期 */
  private LocalDateTime inceptionDate;

  /** 产品描述 */
  private String description;

  /** 是否启用 */
  private Boolean isActive;

  /** 排序序号 */
  private Integer sortOrder;

  /** 创建时间 */
  private LocalDateTime createdAt;

  /** 更新时间 */
  private LocalDateTime updatedAt;

  /**
   * 从实体对象转换为DTO
   *
   * @param entity 产品实体
   * @return 产品DTO
   */
  public static ProductDTO fromEntity(Product entity) {
    ProductDTO dto = new ProductDTO();
    dto.setId(entity.getId());
    dto.setName(entity.getName());
    dto.setCode(entity.getCode());
    dto.setStrategyTypeId(entity.getStrategyTypeId());
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
