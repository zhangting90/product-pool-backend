package com.productpool.backend.service;

import com.productpool.backend.dto.ConfigurationTypeCreateDTO;
import com.productpool.backend.dto.ConfigurationTypeDTO;
import com.productpool.backend.dto.ConfigurationTypeQueryDTO;
import com.productpool.backend.dto.ConfigurationTypeUpdateDTO;

import java.util.List;

public interface ConfigurationTypeService {

  /**
   * 创建配置类型
   *
   * @param createDTO 创建配置类型的DTO
   * @return 创建后的配置类型DTO
   */
  ConfigurationTypeDTO create(ConfigurationTypeCreateDTO createDTO);

  /**
   * 根据ID查询配置类型
   *
   * @param id 配置类型ID
   * @return 配置类型DTO
   */
  ConfigurationTypeDTO findById(Long id);

  /**
   * 查询所有配置类型
   *
   * @return 配置类型DTO列表
   */
  List<ConfigurationTypeDTO> findAll();

  /**
   * 根据条件查询配置类型
   *
   * @param queryDTO 查询条件DTO
   * @return 配置类型DTO列表
   */
  List<ConfigurationTypeDTO> findByQuery(ConfigurationTypeQueryDTO queryDTO);

  /**
   * 更新配置类型
   *
   * @param id 配置类型ID
   * @param updateDTO 更新配置类型的DTO
   * @return 更新后的配置类型DTO
   */
  ConfigurationTypeDTO update(Long id, ConfigurationTypeUpdateDTO updateDTO);

  /**
   * 删除配置类型
   *
   * @param id 配置类型ID
   */
  void delete(Long id);

  /**
   * 查询大分类及其子分类（层级结构）
   *
   * @return 大分类列表，每个大分类包含其子分类
   */
  List<ConfigurationTypeDTO> findMajorTypesWithSubTypes();

  /**
   * 根据父分类ID查询子分类
   *
   * @param parentId 父分类ID
   * @return 子分类列表
   */
  List<ConfigurationTypeDTO> findByParentId(Long parentId);

  /**
   * 查询所有大分类
   *
   * @return 大分类列表
   */
  List<ConfigurationTypeDTO> findMajorTypes();
}
