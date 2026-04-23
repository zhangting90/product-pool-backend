package com.productpool.backend.dto;

import com.productpool.backend.entity.Product;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** 产品响应DTO 用于返回产品的完整信息 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {

  /** 产品编码（主键） */
  private String code;

  /** 产品名称 */
  private String name;

  /** 所属策略类型ID */
  private Long strategyTypeId;

  /** 所属策略类型名称 */
  private String strategyTypeName;

  /** 产品描述 */
  private String description;

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
    dto.setCode(entity.getCode());
    dto.setName(entity.getName());
    dto.setStrategyTypeId(entity.getStrategyTypeId());
    dto.setDescription(entity.getDescription());
    dto.setSortOrder(entity.getSortOrder());
    dto.setCreatedAt(entity.getCreatedAt());
    dto.setUpdatedAt(entity.getUpdatedAt());
    return dto;
  }
}
