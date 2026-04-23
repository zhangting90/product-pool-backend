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

@RestController
@RequestMapping("/api/v1/benchmarks")
@RequiredArgsConstructor
public class BenchmarkController {

  private final BenchmarkService benchmarkService;

  @PostMapping
  public ResponseEntity<Result<BenchmarkDTO>> create(
      @Valid @RequestBody BenchmarkCreateDTO createDTO) {
    BenchmarkDTO dto = benchmarkService.create(createDTO);
    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(Result.success(dto));
  }

  @GetMapping("/{id}")
  public ResponseEntity<Result<BenchmarkDTO>> findById(@PathVariable Long id) {
    BenchmarkDTO dto = benchmarkService.findById(id);
    return ResponseEntity.ok(Result.success(dto));
  }

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

  @PutMapping("/{id}")
  public ResponseEntity<Result<BenchmarkDTO>> update(
      @PathVariable Long id,
      @Valid @RequestBody BenchmarkUpdateDTO updateDTO) {
    BenchmarkDTO dto = benchmarkService.update(id, updateDTO);
    return ResponseEntity.ok(Result.success(dto));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Result<Void>> delete(@PathVariable Long id) {
    benchmarkService.delete(id);
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/configuration-types/{configurationTypeId}")
  public ResponseEntity<Result<List<BenchmarkDTO>>> findByConfigurationTypeId(
      @PathVariable Long configurationTypeId) {
    List<BenchmarkDTO> result = benchmarkService.findByConfigurationTypeId(configurationTypeId);
    return ResponseEntity.ok(Result.success(result));
  }
}
