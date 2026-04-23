package com.productpool.backend.repository;

import com.productpool.backend.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

  Optional<Product> findByCode(String code);

  Page<Product> findByStrategyTypeId(Long strategyTypeId, Pageable pageable);

  Page<Product> findByIsActiveTrueOrderBySortOrderAsc(Pageable pageable);

  @Query("SELECT p FROM Product p WHERE (:name IS NULL OR p.name LIKE %:name%) " +
         "AND (:code IS NULL OR p.code LIKE %:code%) " +
         "AND (:strategyTypeId IS NULL OR p.strategyType.id = :strategyTypeId) " +
         "AND (:riskLevel IS NULL OR p.riskLevel = :riskLevel) " +
         "AND (:isActive IS NULL OR p.isActive = :isActive)")
  Page<Product> findByQuery(
      @Param("name") String name,
      @Param("code") String code,
      @Param("strategyTypeId") Long strategyTypeId,
      @Param("riskLevel") String riskLevel,
      @Param("isActive") Boolean isActive,
      Pageable pageable);

  boolean existsByCode(String code);
}
