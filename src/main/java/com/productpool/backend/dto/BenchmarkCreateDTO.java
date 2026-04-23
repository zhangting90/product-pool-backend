package com.productpool.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BenchmarkCreateDTO {

  @NotBlank(message = "Name is required")
  @Size(max = 100, message = "Name must not exceed 100 characters")
  private String name;

  @NotBlank(message = "Code is required")
  @Size(max = 50, message = "Code must not exceed 50 characters")
  private String code;

  @Size(max = 500, message = "Description must not exceed 500 characters")
  private String description;

  @NotNull(message = "Configuration type ID is required")
  private Long configurationTypeId;

  private Integer sortOrder;
}
