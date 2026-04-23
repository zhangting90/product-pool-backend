package com.productpool.backend.controller;

import com.productpool.backend.dto.Result;
import com.productpool.backend.service.ProductPoolService;
import com.productpool.backend.vo.ProductPoolVO;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** 产品池查询控制器 提供产品池数据的查询接口 基础路径: /api/v1/product-pool */
@RestController
@RequestMapping("/api/v1/product-pool")
@RequiredArgsConstructor
public class ProductPoolController {

  private final ProductPoolService productPoolService;

  /** 查询产品池数据 GET /api/v1/product-pool */
  @GetMapping
  public ResponseEntity<Result<List<ProductPoolVO>>> getProductPool() {
    List<ProductPoolVO> result = productPoolService.getProductPoolData();
    return ResponseEntity.ok(Result.success(result));
  }
}
