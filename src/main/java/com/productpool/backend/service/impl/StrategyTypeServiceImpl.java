package com.productpool.backend.service.impl;

import com.productpool.backend.dto.StrategyTypeCreateDTO;
import com.productpool.backend.dto.StrategyTypeDTO;
import com.productpool.backend.dto.StrategyTypeQueryDTO;
import com.productpool.backend.dto.StrategyTypeUpdateDTO;
import com.productpool.backend.entity.Benchmark;
import com.productpool.backend.entity.StrategyType;
import com.productpool.backend.exception.BusinessLogicException;
import com.productpool.backend.exception.ResourceNotFoundException;
import com.productpool.backend.repository.BenchmarkRepository;
import com.productpool.backend.repository.StrategyTypeRepository;
import com.productpool.backend.service.StrategyTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 策略类型服务实现类
 * <p>实现策略类型的创建、查询、更新、删除等业务逻辑，
 * 包含关联产品检查以确保数据完整性。</p>
 */
@Service
@RequiredArgsConstructor
public class StrategyTypeServiceImpl implements StrategyTypeService {

  private final StrategyTypeRepository strategyTypeRepository;
  private final BenchmarkRepository benchmarkRepository;
  private final ProductRepository productRepository;

  /**
   * 创建策略类型
   * <p>验证关联的业绩对标存在性后创建新策略类型。</p>
   *
   * @param createDTO 创建策略类型的DTO
   * @return 创建后的策略类型DTO
   * @throws ResourceNotFoundException 业绩对标不存在时抛出
   */
  @Override
  @Transactional
  public StrategyTypeDTO create(StrategyTypeCreateDTO createDTO) {
    // 验证业绩对标是否存在
    Benchmark benchmark = benchmarkRepository
        .findById(createDTO.getBenchmarkId())
        .orElseThrow(() -> new ResourceNotFoundException("Benchmark", createDTO.getBenchmarkId()));

    StrategyType entity = new StrategyType();
    entity.setName(createDTO.getName());
    entity.setDescription(createDTO.getDescription());
    entity.setBenchmarkId(benchmark.getId());
    entity.setSortOrder(createDTO.getSortOrder());

    StrategyType saved = strategyTypeRepository.save(entity);
    return StrategyTypeDTO.fromEntity(saved);
  }

  /**
   * 根据ID查询策略类型
   *
   * @param id 策略类型ID
   * @return 策略类型DTO
   * @throws ResourceNotFoundException 策略类型不存在时抛出
   */
  @Override
  public StrategyTypeDTO findById(Long id) {
    StrategyType entity = strategyTypeRepository
        .findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("StrategyType", id));
    return StrategyTypeDTO.fromEntity(entity);
  }

  /**
   * 查询所有策略类型
   *
   * @return 策略类型DTO列表
   */
  @Override
  public List<StrategyTypeDTO> findAll() {
    List<StrategyType> entities = strategyTypeRepository.findAll();
    return entities.stream()
        .map(StrategyTypeDTO::fromEntity)
        .collect(Collectors.toList());
  }

  /**
   * 根据条件查询策略类型
   * <p>支持按业绩对标ID查询，未指定条件则查询全部。</p>
   *
   * @param queryDTO 查询条件DTO
   * @return 策略类型DTO列表
   */
  @Override
  public List<StrategyTypeDTO> findByQuery(StrategyTypeQueryDTO queryDTO) {
    List<StrategyType> entities;

    if (queryDTO.getBenchmarkId() != null) {
      entities = strategyTypeRepository
          .findByBenchmarkIdOrderBySortOrderAsc(queryDTO.getBenchmarkId());
    } else {
      entities = strategyTypeRepository.findAll();
    }

    return entities.stream()
        .map(StrategyTypeDTO::fromEntity)
        .collect(Collectors.toList());
  }

  /**
   * 更新策略类型
   * <p>仅更新非null字段（名称、描述、排序）。</p>
   *
   * @param id 策略类型ID
   * @param updateDTO 更新策略类型的DTO
   * @return 更新后的策略类型DTO
   * @throws ResourceNotFoundException 策略类型不存在时抛出
   */
  @Override
  @Transactional
  public StrategyTypeDTO update(Long id, StrategyTypeUpdateDTO updateDTO) {
    StrategyType entity = strategyTypeRepository
        .findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("StrategyType", id));

    if (updateDTO.getName() != null) {
      entity.setName(updateDTO.getName());
    }
    if (updateDTO.getDescription() != null) {
      entity.setDescription(updateDTO.getDescription());
    }
    if (updateDTO.getSortOrder() != null) {
      entity.setSortOrder(updateDTO.getSortOrder());
    }

    StrategyType updated = strategyTypeRepository.save(entity);
    return StrategyTypeDTO.fromEntity(updated);
  }

  /**
   * 删除策略类型
   * <p>删除前检查是否有关联的产品，存在则阻止删除。</p>
   *
   * @param id 策略类型ID
   * @throws ResourceNotFoundException 策略类型不存在时抛出
   * @throws BusinessLogicException 存在关联产品时抛出
   */
  @Override
  @Transactional
  public void delete(Long id) {
    StrategyType entity = strategyTypeRepository
        .findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("StrategyType", id));

    // 检查是否有关联的产品
    if (productRepository.existsByStrategyTypeId(id)) {
      throw new BusinessLogicException(
          "Cannot delete strategy type with associated products. Please delete products first.");
    }

    strategyTypeRepository.delete(entity);
  }

  /**
   * 根据业绩对标ID查询策略类型
   *
   * @param benchmarkId 业绩对标ID
   * @return 策略类型DTO列表，按排序字段升序排列
   */
  @Override
  public List<StrategyTypeDTO> findByBenchmarkId(Long benchmarkId) {
    List<StrategyType> entities = strategyTypeRepository
        .findByBenchmarkIdOrderBySortOrderAsc(benchmarkId);
    return entities.stream()
        .map(StrategyTypeDTO::fromEntity)
        .collect(Collectors.toList());
  }
}
