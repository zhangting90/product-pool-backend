package com.productpool.backend.repository;

import com.productpool.backend.entity.ConfigurationType;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * 配置类型数据访问层
 *
 * <p>提供配置类型的增删改查及层级结构查询功能。
 */
@Repository
public interface ConfigurationTypeRepository extends JpaRepository<ConfigurationType, Long> {

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
  @Query(
      "SELECT c FROM ConfigurationType c WHERE c.parentId = :parentId OR c.id = :parentId ORDER BY c.sortOrder ASC")
  List<ConfigurationType> findHierarchyByParentId(Long parentId);

  /**
   * 查询所有大分类，按排序字段升序排列
   *
   * @return 大分类列表
   */
  @Query("SELECT c FROM ConfigurationType c WHERE c.isMajor = true ORDER BY c.sortOrder ASC")
  List<ConfigurationType> findMajorTypesWithSubTypes();

  /**
   * 多条件动态查询配置类型（支持名称、是否大分类、父级ID）
   *
   * @param name 配置类型名称（模糊匹配，可为null）
   * @param isMajor 是否为主类型（精确匹配，可为null）
   * @param parentId 父级配置类型ID（精确匹配，可为null）
   * @return 配置类型列表
   */
  @Query(
      "SELECT c FROM ConfigurationType c WHERE (:name IS NULL OR c.name LIKE %:name%) "
          + "AND (:isMajor IS NULL OR c.isMajor = :isMajor) "
          + "AND (:parentId IS NULL OR c.parentId = :parentId) "
          + "ORDER BY c.sortOrder ASC")
  List<ConfigurationType> findByQuery(
      @Param("name") String name,
      @Param("isMajor") Boolean isMajor,
      @Param("parentId") Long parentId);

  /**
   * 判断指定名称的配置类型是否已存在
   *
   * @param name 配置类型名称
   * @return 存在返回true，否则返回false
   */
  boolean existsByName(String name);
}
