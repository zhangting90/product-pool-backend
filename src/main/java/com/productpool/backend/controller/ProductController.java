package com.productpool.backend.controller;

import com.productpool.backend.dto.PageResult;
import com.productpool.backend.dto.ProductCreateDTO;
import com.productpool.backend.dto.ProductDTO;
import com.productpool.backend.dto.ProductQueryDTO;
import com.productpool.backend.dto.ProductUpdateDTO;
import com.productpool.backend.dto.Result;
import com.productpool.backend.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/** 产品管理控制器 提供产品的增删改查 API 接口，支持分页、搜索及多条件筛选 基础路径: /api/v1/products */
@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {

  private final ProductService productService;

  /** 创建产品 POST /api/v1/products */
  @PostMapping
  public ResponseEntity<Result<ProductDTO>> create(@Valid @RequestBody ProductCreateDTO createDTO) {
    ProductDTO dto = productService.create(createDTO);
    return ResponseEntity.status(HttpStatus.CREATED).body(Result.success(dto));
  }

  /** 根据ID查询产品 GET /api/v1/products/{id} */
  @GetMapping("/{id}")
  public ResponseEntity<Result<ProductDTO>> findById(@PathVariable Long id) {
    ProductDTO dto = productService.findById(id);
    return ResponseEntity.ok(Result.success(dto));
  }

  /** 分页查询所有产品，支持排序 GET /api/v1/products */
  @GetMapping
  public ResponseEntity<Result<PageResult<ProductDTO>>> findAll(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size,
      @RequestParam(defaultValue = "id") String sortBy,
      @RequestParam(defaultValue = "asc") String sortDir) {

    Sort sort =
        sortDir.equalsIgnoreCase("desc")
            ? Sort.by(sortBy).descending()
            : Sort.by(sortBy).ascending();

    Pageable pageable = PageRequest.of(page, size, sort);
    Page<ProductDTO> result = productService.findAll(pageable);

    return ResponseEntity.ok(Result.success(toPageResult(result)));
  }

  /** 按条件搜索产品，支持名称、编码、策略类型、风险等级、状态筛选 GET /api/v1/products/search */
  @GetMapping("/search")
  public ResponseEntity<Result<PageResult<ProductDTO>>> search(
      @RequestParam(required = false) String name,
      @RequestParam(required = false) String code,
      @RequestParam(required = false) Long strategyTypeId,
      @RequestParam(required = false) String riskLevel,
      @RequestParam(required = false) Boolean isActive,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size) {

    ProductQueryDTO queryDTO = new ProductQueryDTO();
    queryDTO.setName(name);
    queryDTO.setCode(code);
    queryDTO.setStrategyTypeId(strategyTypeId);
    queryDTO.setRiskLevel(riskLevel);
    queryDTO.setIsActive(isActive);

    Pageable pageable = PageRequest.of(page, size, Sort.by("sortOrder").ascending());
    Page<ProductDTO> result = productService.findByQuery(queryDTO, pageable);

    return ResponseEntity.ok(Result.success(toPageResult(result)));
  }

  /** 更新产品 PUT /api/v1/products/{id} */
  @PutMapping("/{id}")
  public ResponseEntity<Result<ProductDTO>> update(
      @PathVariable Long id, @Valid @RequestBody ProductUpdateDTO updateDTO) {
    ProductDTO dto = productService.update(id, updateDTO);
    return ResponseEntity.ok(Result.success(dto));
  }

  /** 删除产品 DELETE /api/v1/products/{id} */
  @DeleteMapping("/{id}")
  public ResponseEntity<Result<Void>> delete(@PathVariable Long id) {
    productService.delete(id);
    return ResponseEntity.noContent().build();
  }

  /** 根据策略类型ID分页查询产品 GET /api/v1/products/strategy-types/{strategyTypeId} */
  @GetMapping("/strategy-types/{strategyTypeId}")
  public ResponseEntity<Result<PageResult<ProductDTO>>> findByStrategyTypeId(
      @PathVariable Long strategyTypeId,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size) {

    Pageable pageable = PageRequest.of(page, size, Sort.by("sortOrder").ascending());
    Page<ProductDTO> result = productService.findByStrategyTypeId(strategyTypeId, pageable);

    return ResponseEntity.ok(Result.success(toPageResult(result)));
  }

  /** 分页查询活跃状态的产品 GET /api/v1/products/active */
  @GetMapping("/active")
  public ResponseEntity<Result<PageResult<ProductDTO>>> findActiveProducts(
      @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {

    Pageable pageable = PageRequest.of(page, size, Sort.by("sortOrder").ascending());
    Page<ProductDTO> result = productService.findActiveProducts(pageable);

    return ResponseEntity.ok(Result.success(toPageResult(result)));
  }

  /** 将 Spring Page 对象转换为自定义分页结果对象 */
  private <T> PageResult<T> toPageResult(Page<T> page) {
    PageResult<T> pageResult = new PageResult<>();
    pageResult.setContent(page.getContent());
    pageResult.setPageNumber(page.getNumber());
    pageResult.setPageSize(page.getSize());
    pageResult.setTotalElements(page.getTotalElements());
    pageResult.setTotalPages(page.getTotalPages());
    pageResult.setFirst(page.isFirst());
    pageResult.setLast(page.isLast());
    return pageResult;
  }
}
