package com.productpool.backend.repository;

import com.productpool.backend.entity.Benchmark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BenchmarkRepository extends JpaRepository<Benchmark, Long> {

  Optional<Benchmark> findByCode(String code);

  List<Benchmark> findByConfigurationTypeIdOrderBySortOrderAsc(Long configurationTypeId);

  boolean existsByCode(String code);
}
