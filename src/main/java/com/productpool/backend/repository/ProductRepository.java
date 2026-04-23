package com.productpool.backend.repository;

import com.productpool.backend.entity.Product;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * 产品数据访问层
 *
 * <p>提供产品的基本数据访问操作，包括多条件查询、分页查询等。
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, String> {

  /**
   * 根据策略类型ID分页查询产品
   *
   * @param strategyTypeId 策略类型ID
   * @param pageable 分页参数
   * @return 产品分页结果
   */
  Page<Product> findByStrategyTypeId(Long strategyTypeId, Pageable pageable);

  /**
   * 根据策略类型ID查询所有产品
   *
   * @param strategyTypeId 策略类型ID
   * @return 产品列表
   */
  List<Product> findByStrategyTypeId(Long strategyTypeId);

  /**
   * 判断指定策略类型下是否存在产品
   *
   * @param strategyTypeId 策略类型ID
   * @return 存在返回true，否则返回false
   */
  boolean existsByStrategyTypeId(Long strategyTypeId);

  /**
   * 多条件动态查询产品（支持名称、编码、策略类型的模糊或精确匹配）
   *
   * @param name 产品名称（模糊匹配，可为null）
   * @param code 产品编码（模糊匹配，可为null）
   * @param strategyTypeId 策略类型ID（精确匹配，可为null）
   * @param pageable 分页参数
   * @return 产品分页结果
   */
  @Query(
      "SELECT p FROM Product p WHERE (:name IS NULL OR p.name LIKE %:name%) "
          + "AND (:code IS NULL OR p.code LIKE %:code%) "
          + "AND (:strategyTypeId IS NULL OR p.strategyTypeId = :strategyTypeId)")
  Page<Product> findByQuery(
      @Param("name") String name,
      @Param("code") String code,
      @Param("strategyTypeId") Long strategyTypeId,
      Pageable pageable);
}
