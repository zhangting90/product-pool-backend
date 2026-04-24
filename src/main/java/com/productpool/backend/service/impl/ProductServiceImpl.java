package com.productpool.backend.service.impl;

import com.productpool.backend.dto.ProductCreateDTO;
import com.productpool.backend.dto.ProductDTO;
import com.productpool.backend.dto.ProductQueryDTO;
import com.productpool.backend.dto.ProductUpdateDTO;
import com.productpool.backend.entity.Benchmark;
import com.productpool.backend.entity.Product;
import com.productpool.backend.entity.StrategyType;
import com.productpool.backend.exception.DuplicateResourceException;
import com.productpool.backend.exception.ResourceNotFoundException;
import com.productpool.backend.repository.BenchmarkRepository;
import com.productpool.backend.repository.ConfigurationTypeRepository;
import com.productpool.backend.repository.ProductRepository;
import com.productpool.backend.repository.StrategyTypeRepository;
import com.productpool.backend.service.ProductService;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 产品服务实现类
 *
 * <p>实现产品的创建、查询、更新、删除及多条件分页查询等业务逻辑， 包含编码唯一性校验和策略类型关联验证。
 */
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

  private final ProductRepository productRepository;
  private final StrategyTypeRepository strategyTypeRepository;
  private final BenchmarkRepository benchmarkRepository;
  private final ConfigurationTypeRepository configurationTypeRepository;

  /** 填充单个 DTO 的策略类型名称 */
  private ProductDTO enrichSingleWithStrategyTypeName(ProductDTO dto) {
    strategyTypeRepository
        .findById(dto.getStrategyTypeId())
        .ifPresent(st -> dto.setStrategyTypeName(st.getName()));
    return dto;
  }

  /**
   * 根据分类ID或业绩对标ID级联解析出策略类型ID列表。
   *
   * <p>优先级：strategyTypeId > strategyTypeIds > benchmarkId > configurationTypeId，未指定则返回 null。
   */
  private List<Long> resolveStrategyTypeIds(ProductQueryDTO queryDTO) {
    // 1. 直接指定了策略类型ID列表
    if (queryDTO.getStrategyTypeIds() != null && !queryDTO.getStrategyTypeIds().isEmpty()) {
      return queryDTO.getStrategyTypeIds();
    }

    // 2. 直接指定了单个策略类型ID
    if (queryDTO.getStrategyTypeId() != null) {
      return Collections.singletonList(queryDTO.getStrategyTypeId());
    }

    // 3. 指定了业绩对标ID：查该业绩对标下所有策略类型
    if (queryDTO.getBenchmarkId() != null) {
      List<StrategyType> strategyTypes =
          strategyTypeRepository.findByBenchmarkIdOrderBySortOrderAscUpdatedAtAsc(
              queryDTO.getBenchmarkId());
      if (strategyTypes.isEmpty()) {
        return Collections.emptyList();
      }
      return strategyTypes.stream().map(StrategyType::getId).collect(Collectors.toList());
    }

    // 4. 指定了配置分类ID：查该分类下所有业绩对标，再查所有策略类型
    if (queryDTO.getConfigurationTypeId() != null) {
      List<Benchmark> benchmarks =
          benchmarkRepository.findByConfigurationTypeIdOrderBySortOrderAscUpdatedAtAsc(
              queryDTO.getConfigurationTypeId());
      if (benchmarks.isEmpty()) {
        return Collections.emptyList();
      }
      List<Long> benchmarkIds =
          benchmarks.stream().map(Benchmark::getId).collect(Collectors.toList());
      List<StrategyType> strategyTypes =
          strategyTypeRepository.findAll().stream()
              .filter(st -> benchmarkIds.contains(st.getBenchmarkId()))
              .collect(Collectors.toList());
      if (strategyTypes.isEmpty()) {
        return Collections.emptyList();
      }
      return strategyTypes.stream().map(StrategyType::getId).collect(Collectors.toList());
    }

    // 5. 未指定任何筛选条件：返回 null，不过滤
    return null;
  }

  /**
   * 创建产品
   *
   * <p>校验编码唯一性和策略类型存在性后创建新产品。
   *
   * @param createDTO 创建产品的DTO
   * @return 创建后的产品DTO
   * @throws DuplicateResourceException 编码已存在时抛出
   * @throws ResourceNotFoundException 策略类型不存在时抛出
   */
  @Override
  @Transactional
  public ProductDTO create(ProductCreateDTO createDTO) {
    // 检查code是否已存在
    if (productRepository.existsById(createDTO.getCode())) {
      throw new DuplicateResourceException("Product", "code", createDTO.getCode());
    }

    // 验证策略类型是否存在
    StrategyType strategyType =
        strategyTypeRepository
            .findById(createDTO.getStrategyTypeId())
            .orElseThrow(
                () -> new ResourceNotFoundException("StrategyType", createDTO.getStrategyTypeId()));

    Product entity = new Product();
    entity.setCode(createDTO.getCode());
    entity.setName(createDTO.getName());
    entity.setStrategyTypeId(strategyType.getId());
    entity.setDescription(createDTO.getDescription());
    entity.setSortOrder(createDTO.getSortOrder());

    Product saved = productRepository.save(entity);
    ProductDTO dto = ProductDTO.fromEntity(saved);
    dto.setStrategyTypeName(strategyType.getName());
    return dto;
  }

  /**
   * 根据编码查询产品
   *
   * @param code 产品编码
   * @return 产品DTO
   * @throws ResourceNotFoundException 产品不存在时抛出
   */
  @Override
  public ProductDTO findById(String code) {
    Product entity =
        productRepository
            .findById(code)
            .orElseThrow(() -> new ResourceNotFoundException("Product", code));
    return enrichSingleWithStrategyTypeName(ProductDTO.fromEntity(entity));
  }

  /**
   * 分页查询所有产品
   *
   * @param pageable 分页参数
   * @return 产品分页结果
   */
  @Override
  public Page<ProductDTO> findAll(Pageable pageable) {
    Page<Product> entities = productRepository.findAll(pageable);
    return entities.map(entity -> enrichSingleWithStrategyTypeName(ProductDTO.fromEntity(entity)));
  }

  /**
   * 根据条件分页查询产品
   *
   * <p>支持按分类、业绩对标、策略类型、名称、编码等多条件查询。 分类和业绩对标会级联解析为策略类型ID列表进行过滤。
   *
   * @param queryDTO 查询条件DTO
   * @param pageable 分页参数
   * @return 产品分页结果
   */
  @Override
  public Page<ProductDTO> findByQuery(ProductQueryDTO queryDTO, Pageable pageable) {
    List<Long> resolvedStrategyTypeIds = resolveStrategyTypeIds(queryDTO);

    if (resolvedStrategyTypeIds != null && resolvedStrategyTypeIds.isEmpty()) {
      // 筛选条件无匹配的策略类型，返回空分页
      return Page.empty(pageable);
    }

    Page<Product> entities =
        productRepository.findByQuery(
            queryDTO.getName(), queryDTO.getCode(), null, resolvedStrategyTypeIds, pageable);
    return entities.map(entity -> enrichSingleWithStrategyTypeName(ProductDTO.fromEntity(entity)));
  }

  /**
   * 更新产品
   *
   * <p>仅更新非null字段。
   *
   * @param code 产品编码
   * @param updateDTO 更新产品的DTO
   * @return 更新后的产品DTO
   * @throws ResourceNotFoundException 产品不存在时抛出
   */
  @Override
  @Transactional
  public ProductDTO update(String code, ProductUpdateDTO updateDTO) {
    Product entity =
        productRepository
            .findById(code)
            .orElseThrow(() -> new ResourceNotFoundException("Product", code));

    if (updateDTO.getName() != null) {
      entity.setName(updateDTO.getName());
    }
    if (updateDTO.getDescription() != null) {
      entity.setDescription(updateDTO.getDescription());
    }
    if (updateDTO.getSortOrder() != null) {
      entity.setSortOrder(updateDTO.getSortOrder());
    }

    Product updated = productRepository.save(entity);
    return enrichSingleWithStrategyTypeName(ProductDTO.fromEntity(updated));
  }

  /**
   * 删除产品
   *
   * @param code 产品编码
   * @throws ResourceNotFoundException 产品不存在时抛出
   */
  @Override
  @Transactional
  public void delete(String code) {
    Product entity =
        productRepository
            .findById(code)
            .orElseThrow(() -> new ResourceNotFoundException("Product", code));
    productRepository.delete(entity);
  }

  /**
   * 根据策略类型ID分页查询产品
   *
   * @param strategyTypeId 策略类型ID
   * @param pageable 分页参数
   * @return 产品分页结果
   */
  @Override
  public Page<ProductDTO> findByStrategyTypeId(Long strategyTypeId, Pageable pageable) {
    Page<Product> entities = productRepository.findByStrategyTypeId(strategyTypeId, pageable);
    return entities.map(entity -> enrichSingleWithStrategyTypeName(ProductDTO.fromEntity(entity)));
  }
}
