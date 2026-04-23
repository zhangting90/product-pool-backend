package com.productpool.backend.repository;

import com.productpool.backend.entity.StrategyType;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 策略类型数据访问层
 *
 * <p>提供策略类型的基本数据访问操作，包括按业绩对标ID查询等。
 */
@Repository
public interface StrategyTypeRepository extends JpaRepository<StrategyType, Long> {

  /**
   * 根据业绩对标ID查询策略类型，按排序字段升序排列
   *
   * @param benchmarkId 业绩对标ID
   * @return 策略类型列表
   */
  List<StrategyType> findByBenchmarkIdOrderBySortOrderAsc(Long benchmarkId);

  /**
   * 判断指定业绩对标下是否存在策略类型
   *
   * @param benchmarkId 业绩对标ID
   * @return 存在返回true，否则返回false
   */
  boolean existsByBenchmarkId(Long benchmarkId);
}
