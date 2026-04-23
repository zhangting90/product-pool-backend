package com.productpool.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 策略类型创建请求DTO
 * 用于接收前端创建策略类型时的请求参数
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StrategyTypeCreateDTO {

  /** 策略类型名称 */
  @NotBlank(message = "Name is required")
  @Size(max = 100, message = "Name must not exceed 100 characters")
  private String name;

  /** 策略类型描述 */
  @Size(max = 500, message = "Description must not exceed 500 characters")
  private String description;

  /** 所属基准指标ID */
  @NotNull(message = "Benchmark ID is required")
  private Long benchmarkId;

  /** 排序序号 */
  private Integer sortOrder;
}
