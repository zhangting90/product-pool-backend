package com.productpool.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageResult<T> {

  private List<T> content;

  private int pageNumber;

  private int pageSize;

  private long totalElements;

  private int totalPages;

  private boolean first;

  private boolean last;
}
