package com.productpool.backend.service.impl;

import com.productpool.backend.dto.ProductCreateDTO;
import com.productpool.backend.dto.ProductDTO;
import com.productpool.backend.dto.ProductQueryDTO;
import com.productpool.backend.dto.ProductUpdateDTO;
import com.productpool.backend.entity.Product;
import com.productpool.backend.entity.StrategyType;
import com.productpool.backend.exception.DuplicateResourceException;
import com.productpool.backend.exception.ResourceNotFoundException;
import com.productpool.backend.repository.ProductRepository;
import com.productpool.backend.repository.StrategyTypeRepository;
import com.productpool.backend.service.ProductService;
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

  /**
   * 创建产品
   *
   * <p>校验编码唯一性和策略类型存在性后创建新产品，默认激活状态为true。
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
    if (productRepository.existsByCode(createDTO.getCode())) {
      throw new DuplicateResourceException("Product", "code", createDTO.getCode());
    }

    // 验证策略类型是否存在
    StrategyType strategyType =
        strategyTypeRepository
            .findById(createDTO.getStrategyTypeId())
            .orElseThrow(
                () -> new ResourceNotFoundException("StrategyType", createDTO.getStrategyTypeId()));

    Product entity = new Product();
    entity.setName(createDTO.getName());
    entity.setCode(createDTO.getCode());
    entity.setStrategyTypeId(strategyType.getId());
    entity.setRiskLevel(createDTO.getRiskLevel());
    entity.setAnnualReturn(createDTO.getAnnualReturn());
    entity.setVolatility(createDTO.getVolatility());
    entity.setSharpeRatio(createDTO.getSharpeRatio());
    entity.setMaxDrawdown(createDTO.getMaxDrawdown());
    entity.setFundManager(createDTO.getFundManager());
    entity.setFundScale(createDTO.getFundScale());
    entity.setInceptionDate(createDTO.getInceptionDate());
    entity.setDescription(createDTO.getDescription());
    entity.setIsActive(createDTO.getIsActive() != null ? createDTO.getIsActive() : true);
    entity.setSortOrder(createDTO.getSortOrder());

    Product saved = productRepository.save(entity);
    return ProductDTO.fromEntity(saved);
  }

  /**
   * 根据ID查询产品
   *
   * @param id 产品ID
   * @return 产品DTO
   * @throws ResourceNotFoundException 产品不存在时抛出
   */
  @Override
  public ProductDTO findById(Long id) {
    Product entity =
        productRepository
            .findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Product", id));
    return ProductDTO.fromEntity(entity);
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
    return entities.map(ProductDTO::fromEntity);
  }

  /**
   * 根据条件分页查询产品
   *
   * <p>支持按名称、编码、策略类型、风险等级、激活状态等多条件查询。
   *
   * @param queryDTO 查询条件DTO
   * @param pageable 分页参数
   * @return 产品分页结果
   */
  @Override
  public Page<ProductDTO> findByQuery(ProductQueryDTO queryDTO, Pageable pageable) {
    Page<Product> entities =
        productRepository.findByQuery(
            queryDTO.getName(),
            queryDTO.getCode(),
            queryDTO.getStrategyTypeId(),
            queryDTO.getRiskLevel(),
            queryDTO.getIsActive(),
            pageable);
    return entities.map(ProductDTO::fromEntity);
  }

  /**
   * 更新产品
   *
   * <p>仅更新非null字段。
   *
   * @param id 产品ID
   * @param updateDTO 更新产品的DTO
   * @return 更新后的产品DTO
   * @throws ResourceNotFoundException 产品不存在时抛出
   */
  @Override
  @Transactional
  public ProductDTO update(Long id, ProductUpdateDTO updateDTO) {
    Product entity =
        productRepository
            .findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Product", id));

    if (updateDTO.getName() != null) {
      entity.setName(updateDTO.getName());
    }
    if (updateDTO.getDescription() != null) {
      entity.setDescription(updateDTO.getDescription());
    }
    if (updateDTO.getRiskLevel() != null) {
      entity.setRiskLevel(updateDTO.getRiskLevel());
    }
    if (updateDTO.getAnnualReturn() != null) {
      entity.setAnnualReturn(updateDTO.getAnnualReturn());
    }
    if (updateDTO.getVolatility() != null) {
      entity.setVolatility(updateDTO.getVolatility());
    }
    if (updateDTO.getSharpeRatio() != null) {
      entity.setSharpeRatio(updateDTO.getSharpeRatio());
    }
    if (updateDTO.getMaxDrawdown() != null) {
      entity.setMaxDrawdown(updateDTO.getMaxDrawdown());
    }
    if (updateDTO.getFundManager() != null) {
      entity.setFundManager(updateDTO.getFundManager());
    }
    if (updateDTO.getFundScale() != null) {
      entity.setFundScale(updateDTO.getFundScale());
    }
    if (updateDTO.getInceptionDate() != null) {
      entity.setInceptionDate(updateDTO.getInceptionDate());
    }
    if (updateDTO.getIsActive() != null) {
      entity.setIsActive(updateDTO.getIsActive());
    }
    if (updateDTO.getSortOrder() != null) {
      entity.setSortOrder(updateDTO.getSortOrder());
    }

    Product updated = productRepository.save(entity);
    return ProductDTO.fromEntity(updated);
  }

  /**
   * 删除产品
   *
   * @param id 产品ID
   * @throws ResourceNotFoundException 产品不存在时抛出
   */
  @Override
  @Transactional
  public void delete(Long id) {
    Product entity =
        productRepository
            .findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Product", id));
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
    return entities.map(ProductDTO::fromEntity);
  }

  /**
   * 分页查询所有激活状态的产品
   *
   * @param pageable 分页参数
   * @return 激活产品分页结果，按排序字段升序排列
   */
  @Override
  public Page<ProductDTO> findActiveProducts(Pageable pageable) {
    Page<Product> entities = productRepository.findByIsActiveTrueOrderBySortOrderAsc(pageable);
    return entities.map(ProductDTO::fromEntity);
  }
}
