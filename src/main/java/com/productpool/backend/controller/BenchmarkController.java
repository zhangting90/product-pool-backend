package com.productpool.backend.controller;

import com.productpool.backend.dto.BenchmarkCreateDTO;
import com.productpool.backend.dto.BenchmarkDTO;
import com.productpool.backend.dto.BenchmarkQueryDTO;
import com.productpool.backend.dto.BenchmarkUpdateDTO;
import com.productpool.backend.dto.Result;
import com.productpool.backend.service.BenchmarkService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 基准管理控制器
 * 提供基准的增删改查 API 接口，支持按配置类型筛选
 * 基础路径: /api/v1/benchmarks
 */
@RestController
@RequestMapping("/api/v1/benchmarks")
@RequiredArgsConstructor
public class BenchmarkController {

  private final BenchmarkService benchmarkService;

  /** 创建基准 POST /api/v1/benchmarks */
  @PostMapping
  public ResponseEntity<Result<BenchmarkDTO>> create(
      @Valid @RequestBody BenchmarkCreateDTO createDTO) {
    BenchmarkDTO dto = benchmarkService.create(createDTO);
    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(Result.success(dto));
  }

  /** 根据ID查询基准 GET /api/v1/benchmarks/{id} */
  @GetMapping("/{id}")
  public ResponseEntity<Result<BenchmarkDTO>> findById(@PathVariable Long id) {
    BenchmarkDTO dto = benchmarkService.findById(id);
    return ResponseEntity.ok(Result.success(dto));
  }

  /** 查询所有基准，支持按配置类型ID和编码筛选 GET /api/v1/benchmarks */
  @GetMapping
  public ResponseEntity<Result<List<BenchmarkDTO>>> findAll(
      @RequestParam(required = false) Long configurationTypeId,
      @RequestParam(required = false) String code) {

    BenchmarkQueryDTO queryDTO = new BenchmarkQueryDTO();
    queryDTO.setConfigurationTypeId(configurationTypeId);
    queryDTO.setCode(code);

    List<BenchmarkDTO> result = benchmarkService.findByQuery(queryDTO);
    return ResponseEntity.ok(Result.success(result));
  }

  /** 更新基准 PUT /api/v1/benchmarks/{id} */
  @PutMapping("/{id}")
  public ResponseEntity<Result<BenchmarkDTO>> update(
      @PathVariable Long id,
      @Valid @RequestBody BenchmarkUpdateDTO updateDTO) {
    BenchmarkDTO dto = benchmarkService.update(id, updateDTO);
    return ResponseEntity.ok(Result.success(dto));
  }

  /** 删除基准 DELETE /api/v1/benchmarks/{id} */
  @DeleteMapping("/{id}")
  public ResponseEntity<Result<Void>> delete(@PathVariable Long id) {
    benchmarkService.delete(id);
    return ResponseEntity.noContent().build();
  }

  /** 根据配置类型ID查询基准列表 GET /api/v1/benchmarks/configuration-types/{configurationTypeId} */
  @GetMapping("/configuration-types/{configurationTypeId}")
  public ResponseEntity<Result<List<BenchmarkDTO>>> findByConfigurationTypeId(
      @PathVariable Long configurationTypeId) {
    List<BenchmarkDTO> result = benchmarkService.findByConfigurationTypeId(configurationTypeId);
    return ResponseEntity.ok(Result.success(result));
  }
}
