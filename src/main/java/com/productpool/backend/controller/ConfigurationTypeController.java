package com.productpool.backend.controller;

import com.productpool.backend.dto.ConfigurationTypeCreateDTO;
import com.productpool.backend.dto.ConfigurationTypeDTO;
import com.productpool.backend.dto.ConfigurationTypeQueryDTO;
import com.productpool.backend.dto.ConfigurationTypeUpdateDTO;
import com.productpool.backend.dto.Result;
import com.productpool.backend.service.ConfigurationTypeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/configuration-types")
@RequiredArgsConstructor
public class ConfigurationTypeController {

  private final ConfigurationTypeService configurationTypeService;

  @PostMapping
  public ResponseEntity<Result<ConfigurationTypeDTO>> create(
      @Valid @RequestBody ConfigurationTypeCreateDTO createDTO) {
    ConfigurationTypeDTO dto = configurationTypeService.create(createDTO);
    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(Result.success(dto));
  }

  @GetMapping("/{id}")
  public ResponseEntity<Result<ConfigurationTypeDTO>> findById(@PathVariable Long id) {
    ConfigurationTypeDTO dto = configurationTypeService.findById(id);
    return ResponseEntity.ok(Result.success(dto));
  }

  @GetMapping
  public ResponseEntity<Result<List<ConfigurationTypeDTO>>> findAll(
      @RequestParam(required = false) Boolean isMajor,
      @RequestParam(required = false) Long parentId,
      @RequestParam(required = false) String code) {

    ConfigurationTypeQueryDTO queryDTO = new ConfigurationTypeQueryDTO();
    queryDTO.setIsMajor(isMajor);
    queryDTO.setParentId(parentId);
    queryDTO.setCode(code);

    List<ConfigurationTypeDTO> result = configurationTypeService.findByQuery(queryDTO);
    return ResponseEntity.ok(Result.success(result));
  }

  @PutMapping("/{id}")
  public ResponseEntity<Result<ConfigurationTypeDTO>> update(
      @PathVariable Long id,
      @Valid @RequestBody ConfigurationTypeUpdateDTO updateDTO) {
    ConfigurationTypeDTO dto = configurationTypeService.update(id, updateDTO);
    return ResponseEntity.ok(Result.success(dto));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Result<Void>> delete(@PathVariable Long id) {
    configurationTypeService.delete(id);
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/hierarchy")
  public ResponseEntity<Result<List<ConfigurationTypeDTO>>> getHierarchy() {
    List<ConfigurationTypeDTO> result = configurationTypeService.findMajorTypesWithSubTypes();
    return ResponseEntity.ok(Result.success(result));
  }

  @GetMapping("/{parentId}/children")
  public ResponseEntity<Result<List<ConfigurationTypeDTO>>> getChildren(@PathVariable Long parentId) {
    List<ConfigurationTypeDTO> result = configurationTypeService.findByParentId(parentId);
    return ResponseEntity.ok(Result.success(result));
  }

  @GetMapping("/major")
  public ResponseEntity<Result<List<ConfigurationTypeDTO>>> getMajorTypes() {
    List<ConfigurationTypeDTO> result = configurationTypeService.findMajorTypes();
    return ResponseEntity.ok(Result.success(result));
  }
}
