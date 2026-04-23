package com.productpool.backend.repository;

import com.productpool.backend.entity.ConfigurationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 配置类型数据访问层
 * <p>提供配置类型的增删改查及层级结构查询功能。</p>
 */
@Repository
public interface ConfigurationTypeRepository extends JpaRepository<ConfigurationType, Long> {

  /**
   * 根据编码查询配置类型
   *
   * @param code 配置类型编码
   * @return 配置类型的Optional包装
   */
  Optional<ConfigurationType> findByCode(String code);

  /**
   * 查询所有大分类，按排序字段升序排列
   *
   * @return 大分类列表
   */
  List<ConfigurationType> findByIsMajorTrueOrderBySortOrderAsc();

  /**
   * 根据父分类ID查询子分类，按排序字段升序排列
   *
   * @param parentId 父分类ID
   * @return 子分类列表
   */
  List<ConfigurationType> findByParentIdOrderBySortOrderAsc(Long parentId);

  /**
   * 查询所有顶级分类（parentId为null），按排序字段升序排列
   *
   * @return 顶级分类列表
   */
  List<ConfigurationType> findByParentIdNullOrderBySortOrderAsc();

  /**
   * 查询指定父分类及其自身的层级结构，按排序字段升序排列
   *
   * @param parentId 父分类ID
   * @return 配置类型列表（包含父分类及其子分类）
   */
  @Query("SELECT c FROM ConfigurationType c WHERE c.parentId = :parentId OR c.id = :parentId ORDER BY c.sortOrder ASC")
  List<ConfigurationType> findHierarchyByParentId(Long parentId);

  /**
   * 查询所有大分类，按排序字段升序排列
   *
   * @return 大分类列表
   */
  @Query("SELECT c FROM ConfigurationType c WHERE c.isMajor = true ORDER BY c.sortOrder ASC")
  List<ConfigurationType> findMajorTypesWithSubTypes();

  /**
   * 判断指定编码的配置类型是否已存在
   *
   * @param code 配置类型编码
   * @return 存在返回true，否则返回false
   */
  boolean existsByCode(String code);
}
