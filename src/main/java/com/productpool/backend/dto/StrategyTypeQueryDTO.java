package com.productpool.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StrategyTypeQueryDTO {

  private Long id;

  private String name;

  private Long benchmarkId;
}
