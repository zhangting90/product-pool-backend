package com.productpool.backend.repository;

import com.productpool.backend.entity.Benchmark;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * 业绩对标数据访问层
 *
 * <p>提供业绩对标的基本数据访问操作，包括按名称、按配置类型查询等。
 */
@Repository
public interface BenchmarkRepository extends JpaRepository<Benchmark, Long> {

  /**
   * 根据配置类型ID查询业绩对标，按排序字段升序排列
   *
   * @param configurationTypeId 配置类型ID
   * @return 业绩对标列表
   */
  List<Benchmark> findByConfigurationTypeIdOrderBySortOrderAscUpdatedAtAsc(
      Long configurationTypeId);

  /**
   * 多条件动态查询业绩对标（支持名称、配置类型ID）
   *
   * @param name 业绩对标名称（模糊匹配，可为null）
   * @param configurationTypeId 配置类型ID（精确匹配，可为null）
   * @return 业绩对标列表
   */
  @Query(
      "SELECT b FROM Benchmark b WHERE (:name IS NULL OR b.name LIKE %:name%) "
          + "AND (:configurationTypeId IS NULL OR b.configurationTypeId = :configurationTypeId) "
          + "ORDER BY b.sortOrder ASC, b.updatedAt ASC")
  List<Benchmark> findByQuery(
      @Param("name") String name, @Param("configurationTypeId") Long configurationTypeId);

  /**
   * 判断指定名称的业绩对标是否已存在
   *
   * @param name 业绩对标名称
   * @return 存在返回true，否则返回false
   */
  boolean existsByName(String name);

  /**
   * 判断指定配置类型下是否存在业绩对标
   *
   * @param configurationTypeId 配置类型ID
   * @return 存在返回true，否则返回false
   */
  boolean existsByConfigurationTypeId(Long configurationTypeId);
}
