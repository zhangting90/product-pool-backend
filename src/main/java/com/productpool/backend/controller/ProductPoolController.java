package com.productpool.backend.controller;

import com.productpool.backend.dto.Result;
import com.productpool.backend.service.ProductPoolService;
import com.productpool.backend.vo.ProductPoolVO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/product-pool")
@RequiredArgsConstructor
public class ProductPoolController {

  private final ProductPoolService productPoolService;

  @GetMapping
  public ResponseEntity<Result<List<ProductPoolVO>>> getProductPool(
      @RequestParam(defaultValue = "false") boolean activeOnly) {

    List<ProductPoolVO> result = activeOnly ?
        productPoolService.getActiveProductPoolData() :
        productPoolService.getProductPoolData();

    return ResponseEntity.ok(Result.success(result));
  }
}
