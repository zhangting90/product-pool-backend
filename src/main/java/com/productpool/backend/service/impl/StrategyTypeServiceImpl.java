package com.productpool.backend.service.impl;

import com.productpool.backend.dto.StrategyTypeCreateDTO;
import com.productpool.backend.dto.StrategyTypeDTO;
import com.productpool.backend.dto.StrategyTypeQueryDTO;
import com.productpool.backend.dto.StrategyTypeUpdateDTO;
import com.productpool.backend.entity.Benchmark;
import com.productpool.backend.entity.ConfigurationType;
import com.productpool.backend.entity.StrategyType;
import com.productpool.backend.exception.BusinessLogicException;
import com.productpool.backend.exception.ResourceNotFoundException;
import com.productpool.backend.repository.BenchmarkRepository;
import com.productpool.backend.repository.ConfigurationTypeRepository;
import com.productpool.backend.repository.ProductRepository;
import com.productpool.backend.repository.StrategyTypeRepository;
import com.productpool.backend.service.StrategyTypeService;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 策略类型服务实现类
 *
 * <p>实现策略类型的创建、查询、更新、删除等业务逻辑， 包含关联产品检查以确保数据完整性。
 */
@Service
@RequiredArgsConstructor
public class StrategyTypeServiceImpl implements StrategyTypeService {

  private final StrategyTypeRepository strategyTypeRepository;
  private final BenchmarkRepository benchmarkRepository;
  private final ConfigurationTypeRepository configurationTypeRepository;
  private final ProductRepository productRepository;

  /** 批量填充业绩对标名称：避免 N+1 查询 */
  private List<StrategyTypeDTO> enrichWithBenchmarkName(List<StrategyTypeDTO> dtos) {
    if (dtos.isEmpty()) return dtos;
    List<Long> bmIds =
        dtos.stream().map(StrategyTypeDTO::getBenchmarkId).distinct().collect(Collectors.toList());
    Map<Long, String> nameMap =
        benchmarkRepository.findAllById(bmIds).stream()
            .collect(Collectors.toMap(Benchmark::getId, Benchmark::getName));
    dtos.forEach(dto -> dto.setBenchmarkName(nameMap.get(dto.getBenchmarkId())));
    return dtos;
  }

  /** 填充单个 DTO 的业绩对标名称 */
  private StrategyTypeDTO enrichSingleWithBenchmarkName(StrategyTypeDTO dto) {
    benchmarkRepository
        .findById(dto.getBenchmarkId())
        .ifPresent(bm -> dto.setBenchmarkName(bm.getName()));
    return dto;
  }

  /**
   * 根据配置类型层级解析出 benchmarkId 列表
   *
   * <p>优先级：benchmarkId > subTypeId > majorTypeId，未指定则返回 null（不过滤）。
   *
   * @param queryDTO 查询条件
   * @return benchmarkId 列表，null 表示不过滤
   */
  private List<Long> resolveBenchmarkIds(StrategyTypeQueryDTO queryDTO) {
    // 1. 直接指定了 benchmarkId
    if (queryDTO.getBenchmarkId() != null) {
      return Collections.singletonList(queryDTO.getBenchmarkId());
    }

    // 2. 指定了子分类ID：查该子分类下所有业绩对标
    if (queryDTO.getSubTypeId() != null) {
      List<Benchmark> benchmarks =
          benchmarkRepository.findByConfigurationTypeIdOrderBySortOrderAscUpdatedAtAsc(
              queryDTO.getSubTypeId());
      return benchmarks.stream().map(Benchmark::getId).collect(Collectors.toList());
    }

    // 3. 指定了大分类ID：查该大分类下所有子分类，再查子分类下所有业绩对标
    if (queryDTO.getMajorTypeId() != null) {
      List<ConfigurationType> subTypes =
          configurationTypeRepository.findAll().stream()
              .filter(ct -> !ct.getIsMajor() && queryDTO.getMajorTypeId().equals(ct.getParentId()))
              .collect(Collectors.toList());
      if (subTypes.isEmpty()) {
        return Collections.emptyList();
      }
      List<Long> subTypeIds =
          subTypes.stream().map(ConfigurationType::getId).collect(Collectors.toList());
      List<Benchmark> benchmarks =
          benchmarkRepository.findAll().stream()
              .filter(bm -> subTypeIds.contains(bm.getConfigurationTypeId()))
              .collect(Collectors.toList());
      return benchmarks.stream().map(Benchmark::getId).collect(Collectors.toList());
    }

    // 4. 未指定任何筛选条件：返回 null，不过滤
    return null;
  }

  /**
   * 创建策略类型
   *
   * <p>验证关联的业绩对标存在性后创建新策略类型。
   *
   * @param createDTO 创建策略类型的DTO
   * @return 创建后的策略类型DTO
   * @throws ResourceNotFoundException 业绩对标不存在时抛出
   */
  @Override
  @Transactional
  public StrategyTypeDTO create(StrategyTypeCreateDTO createDTO) {
    // 验证业绩对标是否存在
    Benchmark benchmark =
        benchmarkRepository
            .findById(createDTO.getBenchmarkId())
            .orElseThrow(
                () -> new ResourceNotFoundException("Benchmark", createDTO.getBenchmarkId()));

    StrategyType entity = new StrategyType();
    entity.setName(createDTO.getName());
    entity.setDescription(createDTO.getDescription());
    entity.setBenchmarkId(benchmark.getId());
    entity.setSortOrder(createDTO.getSortOrder());

    StrategyType saved = strategyTypeRepository.save(entity);
    StrategyTypeDTO dto = StrategyTypeDTO.fromEntity(saved);
    dto.setBenchmarkName(benchmark.getName());
    return dto;
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
    StrategyType entity =
        strategyTypeRepository
            .findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("StrategyType", id));
    return enrichSingleWithBenchmarkName(StrategyTypeDTO.fromEntity(entity));
  }

  /**
   * 查询所有策略类型
   *
   * @return 策略类型DTO列表
   */
  @Override
  public List<StrategyTypeDTO> findAll() {
    List<StrategyType> entities = strategyTypeRepository.findAll();
    List<StrategyTypeDTO> dtos =
        entities.stream().map(StrategyTypeDTO::fromEntity).collect(Collectors.toList());
    return enrichWithBenchmarkName(dtos);
  }

  /**
   * 根据条件查询策略类型
   *
   * <p>支持按大分类、子分类、业绩对标ID进行多级筛选。 优先级：benchmarkId > subTypeId > majorTypeId。
   *
   * @param queryDTO 查询条件DTO
   * @return 策略类型DTO列表
   */
  @Override
  public List<StrategyTypeDTO> findByQuery(StrategyTypeQueryDTO queryDTO) {
    // 解析出需要筛选的 benchmarkId 列表
    List<Long> benchmarkIds = resolveBenchmarkIds(queryDTO);

    List<StrategyType> entities;
    if (benchmarkIds == null) {
      // 未指定任何筛选条件，查询全部
      entities = strategyTypeRepository.findByQuery(queryDTO.getName(), null);
    } else if (benchmarkIds.isEmpty()) {
      // 筛选条件无匹配的业绩对标，返回空列表
      return Collections.emptyList();
    } else {
      // 按 benchmarkId 列表过滤
      entities =
          strategyTypeRepository.findByQuery(queryDTO.getName(), null).stream()
              .filter(st -> benchmarkIds.contains(st.getBenchmarkId()))
              .collect(Collectors.toList());
    }

    List<StrategyTypeDTO> dtos =
        entities.stream().map(StrategyTypeDTO::fromEntity).collect(Collectors.toList());
    return enrichWithBenchmarkName(dtos);
  }

  /**
   * 更新策略类型
   *
   * <p>仅更新非null字段（名称、描述、排序）。
   *
   * @param id 策略类型ID
   * @param updateDTO 更新策略类型的DTO
   * @return 更新后的策略类型DTO
   * @throws ResourceNotFoundException 策略类型不存在时抛出
   */
  @Override
  @Transactional
  public StrategyTypeDTO update(Long id, StrategyTypeUpdateDTO updateDTO) {
    StrategyType entity =
        strategyTypeRepository
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
    return enrichSingleWithBenchmarkName(StrategyTypeDTO.fromEntity(updated));
  }

  /**
   * 删除策略类型
   *
   * <p>删除前检查是否有关联的产品，存在则阻止删除。
   *
   * @param id 策略类型ID
   * @throws ResourceNotFoundException 策略类型不存在时抛出
   * @throws BusinessLogicException 存在关联产品时抛出
   */
  @Override
  @Transactional
  public void delete(Long id) {
    StrategyType entity =
        strategyTypeRepository
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
    List<StrategyType> entities =
        strategyTypeRepository.findByBenchmarkIdOrderBySortOrderAscUpdatedAtAsc(benchmarkId);
    List<StrategyTypeDTO> dtos =
        entities.stream().map(StrategyTypeDTO::fromEntity).collect(Collectors.toList());
    return enrichWithBenchmarkName(dtos);
  }
}
