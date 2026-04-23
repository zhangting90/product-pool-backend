package com.productpool.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** 产品创建请求DTO 用于接收前端创建产品时的请求参数 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductCreateDTO {

  /** 产品编码 */
  @NotBlank(message = "Code is required")
  @Size(max = 50, message = "Code must not exceed 50 characters")
  private String code;

  /** 产品名称 */
  @NotBlank(message = "Name is required")
  @Size(max = 200, message = "Name must not exceed 200 characters")
  private String name;

  /** 所属策略类型ID */
  @NotNull(message = "Strategy type ID is required")
  private Long strategyTypeId;

  /** 产品描述 */
  @Size(max = 1000, message = "Description must not exceed 1000 characters")
  private String description;

  /** 排序序号 */
  private Integer sortOrder;
}
