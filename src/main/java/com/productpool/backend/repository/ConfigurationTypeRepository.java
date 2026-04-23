package com.productpool.backend.repository;

import com.productpool.backend.entity.ConfigurationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConfigurationTypeRepository extends JpaRepository<ConfigurationType, Long> {

  Optional<ConfigurationType> findByCode(String code);

  List<ConfigurationType> findByIsMajorTrueOrderBySortOrderAsc();

  List<ConfigurationType> findByParentIdOrderBySortOrderAsc(Long parentId);

  List<ConfigurationType> findByParentIdNullOrderBySortOrderAsc();

  @Query("SELECT c FROM ConfigurationType c WHERE c.parentId = :parentId OR c.id = :parentId ORDER BY c.sortOrder ASC")
  List<ConfigurationType> findHierarchyByParentId(Long parentId);

  @Query("SELECT c FROM ConfigurationType c WHERE c.isMajor = true ORDER BY c.sortOrder ASC")
  List<ConfigurationType> findMajorTypesWithSubTypes();

  boolean existsByCode(String code);
}
