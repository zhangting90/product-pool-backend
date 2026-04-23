package com.productpool.backend.repository;

import com.productpool.backend.entity.Benchmark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 业绩对标数据访问层
 * <p>提供业绩对标的基本数据访问操作，包括按编码、按配置类型查询等。</p>
 */
@Repository
public interface BenchmarkRepository extends JpaRepository<Benchmark, Long> {

  /**
   * 根据编码查询业绩对标
   *
   * @param code 业绩对标编码
   * @return 业绩对标的Optional包装
   */
  Optional<Benchmark> findByCode(String code);

  /**
   * 根据配置类型ID查询业绩对标，按排序字段升序排列
   *
   * @param configurationTypeId 配置类型ID
   * @return 业绩对标列表
   */
  List<Benchmark> findByConfigurationTypeIdOrderBySortOrderAsc(Long configurationTypeId);

  /**
   * 判断指定编码的业绩对标是否已存在
   *
   * @param code 业绩对标编码
   * @return 存在返回true，否则返回false
   */
  boolean existsByCode(String code);

  /**
   * 判断指定配置类型下是否存在业绩对标
   *
   * @param configurationTypeId 配置类型ID
   * @return 存在返回true，否则返回false
   */
  boolean existsByConfigurationTypeId(Long configurationTypeId);
}
