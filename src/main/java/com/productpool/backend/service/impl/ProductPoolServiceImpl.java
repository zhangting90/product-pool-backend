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
 * <p>负责组装完整的产品池层级数据。 层级结构为：大分类 -> 小分类 -> 业绩对标 -> 策略类型 -> 产品。
 */
@Service
@RequiredArgsConstructor
public class ProductPoolServiceImpl implements ProductPoolService {

  private final ConfigurationTypeRepository configurationTypeRepository;
  private final ProductRepository productRepository;
  private final BenchmarkRepository benchmarkRepository;
  private final StrategyTypeRepository strategyTypeRepository;

  /**
   * 获取完整的产品池数据
   *
   * @return 产品池视图对象列表
   */
  @Override
  public List<ProductPoolVO> getProductPoolData() {
    List<ConfigurationType> majorTypes =
        configurationTypeRepository.findByIsMajorTrueOrderBySortOrderAscUpdatedAtAsc();

    return majorTypes.stream().map(this::buildProductPoolVO).collect(Collectors.toList());
  }

  private ProductPoolVO buildProductPoolVO(ConfigurationType majorType) {
    ProductPoolVO vo = new ProductPoolVO();
    vo.setConfigurationTypeId(majorType.getId());
    vo.setConfigurationTypeName(majorType.getName());
    vo.setIsMajor(majorType.getIsMajor());
    vo.setParentId(majorType.getParentId());
    vo.setSortOrder(majorType.getSortOrder());

    List<ConfigurationType> subTypes =
        configurationTypeRepository.findByParentIdOrderBySortOrderAscUpdatedAtAsc(
            majorType.getId());

    List<ProductPoolVO.ConfigurationTypeVO> children =
        subTypes.stream().map(this::buildConfigurationTypeVO).collect(Collectors.toList());

    vo.setChildren(children);
    return vo;
  }

  private ProductPoolVO.ConfigurationTypeVO buildConfigurationTypeVO(ConfigurationType subType) {
    ProductPoolVO.ConfigurationTypeVO vo = new ProductPoolVO.ConfigurationTypeVO();
    vo.setId(subType.getId());
    vo.setName(subType.getName());
    vo.setSortOrder(subType.getSortOrder());

    List<Benchmark> benchmarks =
        benchmarkRepository.findByConfigurationTypeIdOrderBySortOrderAscUpdatedAtAsc(
            subType.getId());

    List<ProductPoolVO.BenchmarkVO> benchmarkVOs =
        benchmarks.stream().map(this::buildBenchmarkVO).collect(Collectors.toList());

    vo.setBenchmarks(benchmarkVOs);
    return vo;
  }

  private ProductPoolVO.BenchmarkVO buildBenchmarkVO(Benchmark benchmark) {
    ProductPoolVO.BenchmarkVO vo = new ProductPoolVO.BenchmarkVO();
    vo.setId(benchmark.getId());
    vo.setName(benchmark.getName());
    vo.setSortOrder(benchmark.getSortOrder());

    List<StrategyType> strategyTypes =
        strategyTypeRepository.findByBenchmarkIdOrderBySortOrderAscUpdatedAtAsc(benchmark.getId());

    List<ProductPoolVO.StrategyTypeVO> strategyTypeVOs =
        strategyTypes.stream().map(this::buildStrategyTypeVO).collect(Collectors.toList());

    vo.setStrategyTypes(strategyTypeVOs);
    return vo;
  }

  private ProductPoolVO.StrategyTypeVO buildStrategyTypeVO(StrategyType strategyType) {
    ProductPoolVO.StrategyTypeVO vo = new ProductPoolVO.StrategyTypeVO();
    vo.setId(strategyType.getId());
    vo.setName(strategyType.getName());
    vo.setDescription(strategyType.getDescription());
    vo.setSortOrder(strategyType.getSortOrder());

    List<Product> products = productRepository.findByStrategyTypeId(strategyType.getId());

    List<ProductDTO> productDTOs =
        products.stream().map(ProductDTO::fromEntity).collect(Collectors.toList());

    vo.setProducts(productDTOs);
    return vo;
  }
}
