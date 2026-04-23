package com.productpool.backend.controller;

import com.productpool.backend.dto.Result;
import com.productpool.backend.dto.StrategyTypeCreateDTO;
import com.productpool.backend.dto.StrategyTypeDTO;
import com.productpool.backend.dto.StrategyTypeQueryDTO;
import com.productpool.backend.dto.StrategyTypeUpdateDTO;
import com.productpool.backend.service.StrategyTypeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/strategy-types")
@RequiredArgsConstructor
public class StrategyTypeController {

  private final StrategyTypeService strategyTypeService;

  @PostMapping
  public ResponseEntity<Result<StrategyTypeDTO>> create(
      @Valid @RequestBody StrategyTypeCreateDTO createDTO) {
    StrategyTypeDTO dto = strategyTypeService.create(createDTO);
    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(Result.success(dto));
  }

  @GetMapping("/{id}")
  public ResponseEntity<Result<StrategyTypeDTO>> findById(@PathVariable Long id) {
    StrategyTypeDTO dto = strategyTypeService.findById(id);
    return ResponseEntity.ok(Result.success(dto));
  }

  @GetMapping
  public ResponseEntity<Result<List<StrategyTypeDTO>>> findAll(
      @RequestParam(required = false) Long benchmarkId) {

    StrategyTypeQueryDTO queryDTO = new StrategyTypeQueryDTO();
    queryDTO.setBenchmarkId(benchmarkId);

    List<StrategyTypeDTO> result = strategyTypeService.findByQuery(queryDTO);
    return ResponseEntity.ok(Result.success(result));
  }

  @PutMapping("/{id}")
  public ResponseEntity<Result<StrategyTypeDTO>> update(
      @PathVariable Long id,
      @Valid @RequestBody StrategyTypeUpdateDTO updateDTO) {
    StrategyTypeDTO dto = strategyTypeService.update(id, updateDTO);
    return ResponseEntity.ok(Result.success(dto));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Result<Void>> delete(@PathVariable Long id) {
    strategyTypeService.delete(id);
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/benchmarks/{benchmarkId}")
  public ResponseEntity<Result<List<StrategyTypeDTO>>> findByBenchmarkId(
      @PathVariable Long benchmarkId) {
    List<StrategyTypeDTO> result = strategyTypeService.findByBenchmarkId(benchmarkId);
    return ResponseEntity.ok(Result.success(result));
  }
}
