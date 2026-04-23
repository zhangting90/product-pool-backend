package com.productpool.backend.service.impl;

import com.productpool.backend.dto.ProductDTO;
import com.productpool.backend.entity.Benchmark;
import com.productpool.backend.entity.ConfigurationType;
import com.productpool.backend.entity.Product;
import com.productpool.backend.entity.StrategyType;
import com.productpool.backend.repository.BenchmarkRepository;
import com.productpool.backend.repository.ConfigurationTypeRepository;
import com.productpool.backend.repository.ProductRepository;
import com.productpool.backend.repository.StrategyTypeRepository;
import com.productpool.backend.service.ProductPoolService;
import com.productpool.backend.vo.ProductPoolVO;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 产品池服务实现类
 *
 * <p>负责组装完整的产品池层级数据，支持获取全部产品和仅激活产品两种模式。 层级结构为：大分类 -> 小分类 -> 业绩对标 -> 策略类型 -> 产品。
 */
@Service
@RequiredArgsConstructor
public class ProductPoolServiceImpl implements ProductPoolService {

  private final ConfigurationTypeRepository configurationTypeRepository;
  private final ProductRepository productRepository;
  private final BenchmarkRepository benchmarkRepository;
  private final StrategyTypeRepository strategyTypeRepository;

  /**
   * 获取完整的产品池数据（包含所有产品）
   *
   * @return 产品池视图对象列表
   */
  @Override
  public List<ProductPoolVO> getProductPoolData() {
    // 获取所有大分类
    List<ConfigurationType> majorTypes =
        configurationTypeRepository.findByIsMajorTrueOrderBySortOrderAsc();

    return majorTypes.stream().map(this::buildProductPoolVO).collect(Collectors.toList());
  }

  /**
   * 获取产品池数据（仅包含激活状态的产品）
   *
   * @return 产品池视图对象列表
   */
  @Override
  public List<ProductPoolVO> getActiveProductPoolData() {
    // 获取所有大分类
    List<ConfigurationType> majorTypes =
        configurationTypeRepository.findByIsMajorTrueOrderBySortOrderAsc();

    return majorTypes.stream().map(this::buildActiveProductPoolVO).collect(Collectors.toList());
  }

  /**
   * 构建产品池视图对象（包含所有产品）
   *
   * @param majorType 大分类实体
   * @return 包含子分类及完整层级数据的产品池视图对象
   */
  private ProductPoolVO buildProductPoolVO(ConfigurationType majorType) {
    ProductPoolVO vo = new ProductPoolVO();
    vo.setConfigurationTypeId(majorType.getId());
    vo.setConfigurationTypeName(majorType.getName());
    vo.setConfigurationTypeCode(majorType.getCode());
    vo.setIsMajor(majorType.getIsMajor());
    vo.setParentId(majorType.getParentId());
    vo.setSortOrder(majorType.getSortOrder());

    // 获取该大分类下的所有小分类
    List<ConfigurationType> subTypes =
        configurationTypeRepository.findByParentIdOrderBySortOrderAsc(majorType.getId());

    // 构建子配置类型列表
    List<ProductPoolVO.ConfigurationTypeVO> children =
        subTypes.stream().map(this::buildConfigurationTypeVO).collect(Collectors.toList());

    vo.setChildren(children);
    return vo;
  }

  /**
   * 构建子配置类型视图对象（包含所有产品）
   *
   * @param subType 小分类实体
   * @return 包含业绩对标及完整层级数据的子配置类型视图对象
   */
  private ProductPoolVO.ConfigurationTypeVO buildConfigurationTypeVO(ConfigurationType subType) {
    ProductPoolVO.ConfigurationTypeVO vo = new ProductPoolVO.ConfigurationTypeVO();
    vo.setId(subType.getId());
    vo.setName(subType.getName());
    vo.setCode(subType.getCode());
    vo.setSortOrder(subType.getSortOrder());

    // 获取该小分类下的所有业绩对标
    List<Benchmark> benchmarks =
        benchmarkRepository.findByConfigurationTypeIdOrderBySortOrderAsc(subType.getId());

    // 构建业绩对标列表
    List<ProductPoolVO.BenchmarkVO> benchmarkVOs =
        benchmarks.stream().map(this::buildBenchmarkVO).collect(Collectors.toList());

    vo.setBenchmarks(benchmarkVOs);
    return vo;
  }

  /**
   * 构建业绩对标视图对象（包含所有产品）
   *
   * @param benchmark 业绩对标实体
   * @return 包含策略类型及产品数据的业绩对标视图对象
   */
  private ProductPoolVO.BenchmarkVO buildBenchmarkVO(Benchmark benchmark) {
    ProductPoolVO.BenchmarkVO vo = new ProductPoolVO.BenchmarkVO();
    vo.setId(benchmark.getId());
    vo.setName(benchmark.getName());
    vo.setCode(benchmark.getCode());
    vo.setSortOrder(benchmark.getSortOrder());

    // 获取该业绩对标下的所有策略类型
    List<StrategyType> strategyTypes =
        strategyTypeRepository.findByBenchmarkIdOrderBySortOrderAsc(benchmark.getId());

    // 构建策略类型列表
    List<ProductPoolVO.StrategyTypeVO> strategyTypeVOs =
        strategyTypes.stream().map(this::buildStrategyTypeVO).collect(Collectors.toList());

    vo.setStrategyTypes(strategyTypeVOs);
    return vo;
  }

  /**
   * 构建策略类型视图对象（包含所有产品）
   *
   * @param strategyType 策略类型实体
   * @return 包含所有产品的策略类型视图对象
   */
  private ProductPoolVO.StrategyTypeVO buildStrategyTypeVO(StrategyType strategyType) {
    ProductPoolVO.StrategyTypeVO vo = new ProductPoolVO.StrategyTypeVO();
    vo.setId(strategyType.getId());
    vo.setName(strategyType.getName());
    vo.setDescription(strategyType.getDescription());
    vo.setSortOrder(strategyType.getSortOrder());

    // 获取该策略类型下的所有产品
    List<Product> products = productRepository.findByStrategyTypeId(strategyType.getId());

    // 构建产品列表
    List<ProductDTO> productDTOs =
        products.stream().map(ProductDTO::fromEntity).collect(Collectors.toList());

    vo.setProducts(productDTOs);
    return vo;
  }

  /**
   * 构建产品池视图对象（仅包含激活产品）
   *
   * @param majorType 大分类实体
   * @return 包含子分类及激活产品的产品池视图对象
   */
  private ProductPoolVO buildActiveProductPoolVO(ConfigurationType majorType) {
    ProductPoolVO vo = new ProductPoolVO();
    vo.setConfigurationTypeId(majorType.getId());
    vo.setConfigurationTypeName(majorType.getName());
    vo.setConfigurationTypeCode(majorType.getCode());
    vo.setIsMajor(majorType.getIsMajor());
    vo.setParentId(majorType.getParentId());
    vo.setSortOrder(majorType.getSortOrder());

    // 获取该大分类下的所有小分类
    List<ConfigurationType> subTypes =
        configurationTypeRepository.findByParentIdOrderBySortOrderAsc(majorType.getId());

    // 构建子配置类型列表
    List<ProductPoolVO.ConfigurationTypeVO> children =
        subTypes.stream().map(this::buildActiveConfigurationTypeVO).collect(Collectors.toList());

    vo.setChildren(children);
    return vo;
  }

  /**
   * 构建子配置类型视图对象（仅包含激活产品）
   *
   * @param subType 小分类实体
   * @return 包含业绩对标及激活产品的子配置类型视图对象
   */
  private ProductPoolVO.ConfigurationTypeVO buildActiveConfigurationTypeVO(
      ConfigurationType subType) {
    ProductPoolVO.ConfigurationTypeVO vo = new ProductPoolVO.ConfigurationTypeVO();
    vo.setId(subType.getId());
    vo.setName(subType.getName());
    vo.setCode(subType.getCode());
    vo.setSortOrder(subType.getSortOrder());

    // 获取该小分类下的所有业绩对标
    List<Benchmark> benchmarks =
        benchmarkRepository.findByConfigurationTypeIdOrderBySortOrderAsc(subType.getId());

    // 构建业绩对标列表
    List<ProductPoolVO.BenchmarkVO> benchmarkVOs =
        benchmarks.stream().map(this::buildActiveBenchmarkVO).collect(Collectors.toList());

    vo.setBenchmarks(benchmarkVOs);
    return vo;
  }

  /**
   * 构建业绩对标视图对象（仅包含激活产品）
   *
   * @param benchmark 业绩对标实体
   * @return 包含策略类型及激活产品的业绩对标视图对象
   */
  private ProductPoolVO.BenchmarkVO buildActiveBenchmarkVO(Benchmark benchmark) {
    ProductPoolVO.BenchmarkVO vo = new ProductPoolVO.BenchmarkVO();
    vo.setId(benchmark.getId());
    vo.setName(benchmark.getName());
    vo.setCode(benchmark.getCode());
    vo.setSortOrder(benchmark.getSortOrder());

    // 获取该业绩对标下的所有策略类型
    List<StrategyType> strategyTypes =
        strategyTypeRepository.findByBenchmarkIdOrderBySortOrderAsc(benchmark.getId());

    // 构建策略类型列表
    List<ProductPoolVO.StrategyTypeVO> strategyTypeVOs =
        strategyTypes.stream().map(this::buildActiveStrategyTypeVO).collect(Collectors.toList());

    vo.setStrategyTypes(strategyTypeVOs);
    return vo;
  }

  /**
   * 构建策略类型视图对象（仅包含激活产品）
   *
   * @param strategyType 策略类型实体
   * @return 仅包含激活产品的策略类型视图对象
   */
  private ProductPoolVO.StrategyTypeVO buildActiveStrategyTypeVO(StrategyType strategyType) {
    ProductPoolVO.StrategyTypeVO vo = new ProductPoolVO.StrategyTypeVO();
    vo.setId(strategyType.getId());
    vo.setName(strategyType.getName());
    vo.setDescription(strategyType.getDescription());
    vo.setSortOrder(strategyType.getSortOrder());

    // 获取该策略类型下的所有激活产品
    List<Product> products =
        productRepository.findByStrategyTypeId(strategyType.getId()).stream()
            .filter(Product::getIsActive)
            .collect(Collectors.toList());

    // 构建产品列表
    List<ProductDTO> productDTOs =
        products.stream().map(ProductDTO::fromEntity).collect(Collectors.toList());

    vo.setProducts(productDTOs);
    return vo;
  }
}
