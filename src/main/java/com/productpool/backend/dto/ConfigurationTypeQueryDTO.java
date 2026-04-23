package com.productpool.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** 配置类型查询请求DTO 用于接收前端查询配置类型列表时的筛选条件 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConfigurationTypeQueryDTO {

  /** 配置类型ID */
  private Long id;

  /** 配置类型名称（模糊查询） */
  private String name;

  /** 是否为主类型 */
  private Boolean isMajor;

  /** 父级配置类型ID */
  private Long parentId;
}
