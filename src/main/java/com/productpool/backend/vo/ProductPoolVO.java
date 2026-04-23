package com.productpool.backend.vo;

import com.productpool.backend.dto.BenchmarkDTO;
import com.productpool.backend.dto.ProductDTO;
import com.productpool.backend.dto.StrategyTypeDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 产品池视图对象
 * 包含完整的层级结构数据：配置类型 → 业绩对标 → 策略类型 → 产品
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductPoolVO {

  /**
   * 配置类型ID
   */
  private Long configurationTypeId;

  /**
   * 配置类型名称
   */
  private String configurationTypeName;

  /**
   * 配置类型代码
   */
  private String configurationTypeCode;

  /**
   * 是否为大分类
   */
  private Boolean isMajor;

  /**
   * 父分类ID
   */
  private Long parentId;

  /**
   * 排序
   */
  private Integer sortOrder;

  /**
   * 子配置类型列表（仅大分类有）
   */
  private List<ConfigurationTypeVO> children;

  /**
   * 业绩对标列表（仅小分类有）
   */
  private List<BenchmarkVO> benchmarks;

  /**
   * 内部使用的配置类型视图对象
   */
  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  public static class ConfigurationTypeVO {
    private Long id;
    private String name;
    private String code;
    private Integer sortOrder;
    private List<BenchmarkVO> benchmarks;
  }

  /**
   * 内部使用的业绩对标视图对象
   */
  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  public static class BenchmarkVO {
    private Long id;
    private String name;
    private String code;
    private Integer sortOrder;
    private List<StrategyTypeVO> strategyTypes;
  }

  /**
   * 内部使用的策略类型视图对象
   */
  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  public static class StrategyTypeVO {
    private Long id;
    private String name;
    private String description;
    private Integer sortOrder;
    private List<ProductDTO> products;
  }
}
