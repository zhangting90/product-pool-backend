package com.productpool.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConfigurationTypeQueryDTO {

  private Long id;

  private String name;

  private String code;

  private Boolean isMajor;

  private Long parentId;
}
