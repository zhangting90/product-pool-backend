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

/**
 * 配置类型管理控制器
 * 提供配置类型的增删改查 API 接口，支持层级结构查询
 * 基础路径: /api/v1/configuration-types
 */
@RestController
@RequestMapping("/api/v1/configuration-types")
@RequiredArgsConstructor
public class ConfigurationTypeController {

  private final ConfigurationTypeService configurationTypeService;

  /** 创建配置类型 POST /api/v1/configuration-types */
  @PostMapping
  public ResponseEntity<Result<ConfigurationTypeDTO>> create(
      @Valid @RequestBody ConfigurationTypeCreateDTO createDTO) {
    ConfigurationTypeDTO dto = configurationTypeService.create(createDTO);
    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(Result.success(dto));
  }

  /** 根据ID查询配置类型 GET /api/v1/configuration-types/{id} */
  @GetMapping("/{id}")
  public ResponseEntity<Result<ConfigurationTypeDTO>> findById(@PathVariable Long id) {
    ConfigurationTypeDTO dto = configurationTypeService.findById(id);
    return ResponseEntity.ok(Result.success(dto));
  }

  /** 查询所有配置类型，支持按是否主要类型、父级ID、编码筛选 GET /api/v1/configuration-types */
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

  /** 更新配置类型 PUT /api/v1/configuration-types/{id} */
  @PutMapping("/{id}")
  public ResponseEntity<Result<ConfigurationTypeDTO>> update(
      @PathVariable Long id,
      @Valid @RequestBody ConfigurationTypeUpdateDTO updateDTO) {
    ConfigurationTypeDTO dto = configurationTypeService.update(id, updateDTO);
    return ResponseEntity.ok(Result.success(dto));
  }

  /** 删除配置类型 DELETE /api/v1/configuration-types/{id} */
  @DeleteMapping("/{id}")
  public ResponseEntity<Result<Void>> delete(@PathVariable Long id) {
    configurationTypeService.delete(id);
    return ResponseEntity.noContent().build();
  }

  /** 获取配置类型层级结构（主要类型及其子类型） GET /api/v1/configuration-types/hierarchy */
  @GetMapping("/hierarchy")
  public ResponseEntity<Result<List<ConfigurationTypeDTO>>> getHierarchy() {
    List<ConfigurationTypeDTO> result = configurationTypeService.findMajorTypesWithSubTypes();
    return ResponseEntity.ok(Result.success(result));
  }

  /** 根据父级ID查询子配置类型 GET /api/v1/configuration-types/{parentId}/children */
  @GetMapping("/{parentId}/children")
  public ResponseEntity<Result<List<ConfigurationTypeDTO>>> getChildren(@PathVariable Long parentId) {
    List<ConfigurationTypeDTO> result = configurationTypeService.findByParentId(parentId);
    return ResponseEntity.ok(Result.success(result));
  }

  /** 查询所有主要配置类型 GET /api/v1/configuration-types/major */
  @GetMapping("/major")
  public ResponseEntity<Result<List<ConfigurationTypeDTO>>> getMajorTypes() {
    List<ConfigurationTypeDTO> result = configurationTypeService.findMajorTypes();
    return ResponseEntity.ok(Result.success(result));
  }
}
