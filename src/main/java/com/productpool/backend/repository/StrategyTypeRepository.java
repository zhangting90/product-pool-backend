package com.productpool.backend.repository;

import com.productpool.backend.entity.StrategyType;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * 策略类型数据访问层
 *
 * <p>提供策略类型的基本数据访问操作，包括按名称、按业绩对标ID查询等。
 */
@Repository
public interface StrategyTypeRepository extends JpaRepository<StrategyType, Long> {

  /**
   * 根据业绩对标ID查询策略类型，按排序字段升序排列
   *
   * @param benchmarkId 业绩对标ID
   * @return 策略类型列表
   */
  List<StrategyType> findByBenchmarkIdOrderBySortOrderAscUpdatedAtAsc(Long benchmarkId);

  /**
   * 多条件动态查询策略类型（支持名称、业绩对标ID）
   *
   * @param name 策略类型名称（模糊匹配，可为null）
   * @param benchmarkId 业绩对标ID（精确匹配，可为null）
   * @return 策略类型列表
   */
  @Query(
      "SELECT s FROM StrategyType s WHERE (:name IS NULL OR s.name LIKE %:name%) "
          + "AND (:benchmarkId IS NULL OR s.benchmarkId = :benchmarkId) "
          + "ORDER BY s.sortOrder ASC, s.updatedAt ASC")
  List<StrategyType> findByQuery(
      @Param("name") String name, @Param("benchmarkId") Long benchmarkId);

  /**
   * 判断指定业绩对标下是否存在策略类型
   *
   * @param benchmarkId 业绩对标ID
   * @return 存在返回true，否则返回false
   */
  boolean existsByBenchmarkId(Long benchmarkId);
}
