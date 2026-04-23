package com.productpool.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 配置类型创建请求DTO
 * 用于接收前端创建配置类型时的请求参数
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConfigurationTypeCreateDTO {

  /** 配置类型名称 */
  @NotBlank(message = "Name is required")
  @Size(max = 100, message = "Name must not exceed 100 characters")
  private String name;

  /** 配置类型编码 */
  @NotBlank(message = "Code is required")
  @Size(max = 50, message = "Code must not exceed 50 characters")
  private String code;

  /** 配置类型描述 */
  @Size(max = 500, message = "Description must not exceed 500 characters")
  private String description;

  /** 是否为主类型 */
  @NotNull(message = "isMajor is required")
  private Boolean isMajor;

  /** 父级配置类型ID */
  private Long parentId;

  /** 排序序号 */
  private Integer sortOrder;
}
