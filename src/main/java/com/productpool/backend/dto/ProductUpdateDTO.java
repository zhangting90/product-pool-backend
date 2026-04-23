package com.productpool.backend.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** 产品更新请求DTO 用于接收前端更新产品时的请求参数 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductUpdateDTO {

  /** 产品名称 */
  @Size(max = 200, message = "Name must not exceed 200 characters")
  private String name;

  /** 产品描述 */
  @Size(max = 1000, message = "Description must not exceed 1000 characters")
  private String description;

  /** 排序序号 */
  private Integer sortOrder;
}
