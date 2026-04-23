package com.productpool.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** 基准指标创建请求DTO 用于接收前端创建基准指标时的请求参数 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BenchmarkCreateDTO {

  /** 基准指标名称 */
  @NotBlank(message = "Name is required")
  @Size(max = 100, message = "Name must not exceed 100 characters")
  private String name;

  /** 基准指标编码 */
  @NotBlank(message = "Code is required")
  @Size(max = 50, message = "Code must not exceed 50 characters")
  private String code;

  /** 基准指标描述 */
  @Size(max = 500, message = "Description must not exceed 500 characters")
  private String description;

  /** 所属配置类型ID */
  @NotNull(message = "Configuration type ID is required")
  private Long configurationTypeId;

  /** 排序序号 */
  private Integer sortOrder;
}
