package com.productpool.backend.dto;

import com.productpool.backend.entity.StrategyType;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** 策略类型响应DTO 用于返回策略类型的完整信息，包含关联的产品列表 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StrategyTypeDTO {

  /** 策略类型ID */
  private Long id;

  /** 策略类型名称 */
  private String name;

  /** 策略类型描述 */
  private String description;

  /** 所属基准指标ID */
  private Long benchmarkId;

  /** 所属基准指标名称 */
  private String benchmarkName;

  /** 排序序号 */
  private Integer sortOrder;

  /** 创建时间 */
  private LocalDateTime createdAt;

  /** 更新时间 */
  private LocalDateTime updatedAt;

  /** 关联的产品列表 */
  private List<ProductDTO> products;

  /**
   * 从实体对象转换为DTO
   *
   * @param entity 策略类型实体
   * @return 策略类型DTO
   */
  public static StrategyTypeDTO fromEntity(StrategyType entity) {
    StrategyTypeDTO dto = new StrategyTypeDTO();
    dto.setId(entity.getId());
    dto.setName(entity.getName());
    dto.setDescription(entity.getDescription());
    dto.setBenchmarkId(entity.getBenchmarkId());
    dto.setSortOrder(entity.getSortOrder());
    dto.setCreatedAt(entity.getCreatedAt());
    dto.setUpdatedAt(entity.getUpdatedAt());
    return dto;
  }
}
