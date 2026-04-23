package com.productpool.backend.controller;

import com.productpool.backend.dto.Result;
import com.productpool.backend.dto.StrategyTypeCreateDTO;
import com.productpool.backend.dto.StrategyTypeDTO;
import com.productpool.backend.dto.StrategyTypeQueryDTO;
import com.productpool.backend.dto.StrategyTypeUpdateDTO;
import com.productpool.backend.service.StrategyTypeService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/** 策略类型管理控制器 提供策略类型的增删改查 API 接口，支持按基准筛选 基础路径: /api/v1/strategy-types */
@RestController
@RequestMapping("/api/v1/strategy-types")
@RequiredArgsConstructor
public class StrategyTypeController {

  private final StrategyTypeService strategyTypeService;

  /** 创建策略类型 POST /api/v1/strategy-types */
  @PostMapping
  public ResponseEntity<Result<StrategyTypeDTO>> create(
      @Valid @RequestBody StrategyTypeCreateDTO createDTO) {
    StrategyTypeDTO dto = strategyTypeService.create(createDTO);
    return ResponseEntity.status(HttpStatus.CREATED).body(Result.success(dto));
  }

  /** 根据ID查询策略类型 GET /api/v1/strategy-types/{id} */
  @GetMapping("/{id}")
  public ResponseEntity<Result<StrategyTypeDTO>> findById(@PathVariable Long id) {
    StrategyTypeDTO dto = strategyTypeService.findById(id);
    return ResponseEntity.ok(Result.success(dto));
  }

  /** 查询所有策略类型，支持按基准ID筛选 GET /api/v1/strategy-types */
  @GetMapping
  public ResponseEntity<Result<List<StrategyTypeDTO>>> findAll(
      @RequestParam(required = false) Long benchmarkId) {

    StrategyTypeQueryDTO queryDTO = new StrategyTypeQueryDTO();
    queryDTO.setBenchmarkId(benchmarkId);

    List<StrategyTypeDTO> result = strategyTypeService.findByQuery(queryDTO);
    return ResponseEntity.ok(Result.success(result));
  }

  /** 更新策略类型 PUT /api/v1/strategy-types/{id} */
  @PutMapping("/{id}")
  public ResponseEntity<Result<StrategyTypeDTO>> update(
      @PathVariable Long id, @Valid @RequestBody StrategyTypeUpdateDTO updateDTO) {
    StrategyTypeDTO dto = strategyTypeService.update(id, updateDTO);
    return ResponseEntity.ok(Result.success(dto));
  }

  /** 删除策略类型 DELETE /api/v1/strategy-types/{id} */
  @DeleteMapping("/{id}")
  public ResponseEntity<Result<Void>> delete(@PathVariable Long id) {
    strategyTypeService.delete(id);
    return ResponseEntity.noContent().build();
  }

  /** 根据基准ID查询策略类型列表 GET /api/v1/strategy-types/benchmarks/{benchmarkId} */
  @GetMapping("/benchmarks/{benchmarkId}")
  public ResponseEntity<Result<List<StrategyTypeDTO>>> findByBenchmarkId(
      @PathVariable Long benchmarkId) {
    List<StrategyTypeDTO> result = strategyTypeService.findByBenchmarkId(benchmarkId);
    return ResponseEntity.ok(Result.success(result));
  }
}
