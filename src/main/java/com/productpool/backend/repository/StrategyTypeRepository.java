package com.productpool.backend.repository;

import com.productpool.backend.entity.StrategyType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StrategyTypeRepository extends JpaRepository<StrategyType, Long> {

  List<StrategyType> findByBenchmarkIdOrderBySortOrderAsc(Long benchmarkId);

  boolean existsByBenchmarkId(Long benchmarkId);
}
