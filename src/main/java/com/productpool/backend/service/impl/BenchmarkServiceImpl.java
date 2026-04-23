package com.productpool.backend.service.impl;

import com.productpool.backend.dto.BenchmarkCreateDTO;
import com.productpool.backend.dto.BenchmarkDTO;
import com.productpool.backend.dto.BenchmarkQueryDTO;
import com.productpool.backend.dto.BenchmarkUpdateDTO;
import com.productpool.backend.entity.Benchmark;
import com.productpool.backend.entity.ConfigurationType;
import com.productpool.backend.exception.BusinessLogicException;
import com.productpool.backend.exception.DuplicateResourceException;
import com.productpool.backend.exception.ResourceNotFoundException;
import com.productpool.backend.repository.BenchmarkRepository;
import com.productpool.backend.repository.ConfigurationTypeRepository;
import com.productpool.backend.service.BenchmarkService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 业绩对标服务实现类
 *
 * <p>实现业绩对标的创建、查询、更新、删除等业务逻辑， 包含编码唯一性校验和级联删除检查。
 */
@Service
@RequiredArgsConstructor
public class BenchmarkServiceImpl implements BenchmarkService {

  private final BenchmarkRepository benchmarkRepository;
  private final ConfigurationTypeRepository configurationTypeRepository;
  private final StrategyTypeRepository strategyTypeRepository;

  /**
   * 创建业绩对标
   *
   * <p>校验编码唯一性和配置类型存在性后创建新业绩对标。
   *
   * @param createDTO 创建业绩对标的DTO
   * @return 创建后的业绩对标DTO
   * @throws DuplicateResourceException 编码已存在时抛出
   * @throws ResourceNotFoundException 配置类型不存在时抛出
   */
  @Override
  @Transactional
  public BenchmarkDTO create(BenchmarkCreateDTO createDTO) {
    // 检查code是否已存在
    if (benchmarkRepository.existsByCode(createDTO.getCode())) {
      throw new DuplicateResourceException("Benchmark", "code", createDTO.getCode());
    }

    // 验证配置类型是否存在
    ConfigurationType configurationType =
        configurationTypeRepository
            .findById(createDTO.getConfigurationTypeId())
            .orElseThrow(
                () ->
                    new ResourceNotFoundException(
                        "ConfigurationType", createDTO.getConfigurationTypeId()));

    Benchmark entity = new Benchmark();
    entity.setName(createDTO.getName());
    entity.setCode(createDTO.getCode());
    entity.setDescription(createDTO.getDescription());
    entity.setConfigurationTypeId(configurationType.getId());
    entity.setSortOrder(createDTO.getSortOrder());

    Benchmark saved = benchmarkRepository.save(entity);
    return BenchmarkDTO.fromEntity(saved);
  }

  /**
   * 根据ID查询业绩对标
   *
   * @param id 业绩对标ID
   * @return 业绩对标DTO
   * @throws ResourceNotFoundException 业绩对标不存在时抛出
   */
  @Override
  public BenchmarkDTO findById(Long id) {
    Benchmark entity =
        benchmarkRepository
            .findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Benchmark", id));
    return BenchmarkDTO.fromEntity(entity);
  }

  /**
   * 查询所有业绩对标
   *
   * @return 业绩对标DTO列表
   */
  @Override
  public List<BenchmarkDTO> findAll() {
    List<Benchmark> entities = benchmarkRepository.findAll();
    return entities.stream().map(BenchmarkDTO::fromEntity).collect(Collectors.toList());
  }

  /**
   * 根据条件查询业绩对标
   *
   * <p>支持按配置类型ID或编码进行条件查询。
   *
   * @param queryDTO 查询条件DTO
   * @return 业绩对标DTO列表
   */
  @Override
  public List<BenchmarkDTO> findByQuery(BenchmarkQueryDTO queryDTO) {
    List<Benchmark> entities;

    if (queryDTO.getConfigurationTypeId() != null) {
      entities =
          benchmarkRepository.findByConfigurationTypeIdOrderBySortOrderAsc(
              queryDTO.getConfigurationTypeId());
    } else if (queryDTO.getCode() != null) {
      Benchmark entity =
          benchmarkRepository
              .findByCode(queryDTO.getCode())
              .orElseThrow(() -> new ResourceNotFoundException("Benchmark", queryDTO.getCode()));
      return List.of(BenchmarkDTO.fromEntity(entity));
    } else {
      entities = benchmarkRepository.findAll();
    }

    return entities.stream().map(BenchmarkDTO::fromEntity).collect(Collectors.toList());
  }

  /**
   * 更新业绩对标
   *
   * <p>仅更新非null字段（名称、描述、排序）。
   *
   * @param id 业绩对标ID
   * @param updateDTO 更新业绩对标的DTO
   * @return 更新后的业绩对标DTO
   * @throws ResourceNotFoundException 业绩对标不存在时抛出
   */
  @Override
  @Transactional
  public BenchmarkDTO update(Long id, BenchmarkUpdateDTO updateDTO) {
    Benchmark entity =
        benchmarkRepository
            .findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Benchmark", id));

    if (updateDTO.getName() != null) {
      entity.setName(updateDTO.getName());
    }
    if (updateDTO.getDescription() != null) {
      entity.setDescription(updateDTO.getDescription());
    }
    if (updateDTO.getSortOrder() != null) {
      entity.setSortOrder(updateDTO.getSortOrder());
    }

    Benchmark updated = benchmarkRepository.save(entity);
    return BenchmarkDTO.fromEntity(updated);
  }

  /**
   * 删除业绩对标
   *
   * <p>删除前检查是否有关联的策略类型，存在则阻止删除。
   *
   * @param id 业绩对标ID
   * @throws ResourceNotFoundException 业绩对标不存在时抛出
   * @throws BusinessLogicException 存在关联策略类型时抛出
   */
  @Override
  @Transactional
  public void delete(Long id) {
    Benchmark entity =
        benchmarkRepository
            .findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Benchmark", id));

    // 检查是否有关联的策略类型
    if (strategyTypeRepository.existsByBenchmarkId(id)) {
      throw new BusinessLogicException(
          "Cannot delete benchmark with associated strategy types. Please delete strategy types first.");
    }

    benchmarkRepository.delete(entity);
  }

  /**
   * 根据配置类型ID查询业绩对标
   *
   * @param configurationTypeId 配置类型ID
   * @return 业绩对标DTO列表，按排序字段升序排列
   */
  @Override
  public List<BenchmarkDTO> findByConfigurationTypeId(Long configurationTypeId) {
    List<Benchmark> entities =
        benchmarkRepository.findByConfigurationTypeIdOrderBySortOrderAsc(configurationTypeId);
    return entities.stream().map(BenchmarkDTO::fromEntity).collect(Collectors.toList());
  }
}
