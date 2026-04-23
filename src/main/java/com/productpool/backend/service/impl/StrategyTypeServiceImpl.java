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

@Service
@RequiredArgsConstructor
public class StrategyTypeServiceImpl implements StrategyTypeService {

  private final StrategyTypeRepository strategyTypeRepository;
  private final BenchmarkRepository benchmarkRepository;

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
    entity.setBenchmark(benchmark);
    entity.setSortOrder(createDTO.getSortOrder());

    StrategyType saved = strategyTypeRepository.save(entity);
    return StrategyTypeDTO.fromEntity(saved);
  }

  @Override
  public StrategyTypeDTO findById(Long id) {
    StrategyType entity = strategyTypeRepository
        .findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("StrategyType", id));
    return StrategyTypeDTO.fromEntity(entity);
  }

  @Override
  public List<StrategyTypeDTO> findAll() {
    List<StrategyType> entities = strategyTypeRepository.findAll();
    return entities.stream()
        .map(StrategyTypeDTO::fromEntity)
        .collect(Collectors.toList());
  }

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

  @Override
  @Transactional
  public void delete(Long id) {
    StrategyType entity = strategyTypeRepository
        .findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("StrategyType", id));

    // 检查是否有关联的产品
    if (!entity.getProducts().isEmpty()) {
      throw new BusinessLogicException(
          "Cannot delete strategy type with associated products. Please delete products first.");
    }

    strategyTypeRepository.delete(entity);
  }

  @Override
  public List<StrategyTypeDTO> findByBenchmarkId(Long benchmarkId) {
    List<StrategyType> entities = strategyTypeRepository
        .findByBenchmarkIdOrderBySortOrderAsc(benchmarkId);
    return entities.stream()
        .map(StrategyTypeDTO::fromEntity)
        .collect(Collectors.toList());
  }
}
