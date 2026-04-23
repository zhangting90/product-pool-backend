package com.productpool.backend.service.impl;

import com.productpool.backend.dto.ConfigurationTypeCreateDTO;
import com.productpool.backend.dto.ConfigurationTypeDTO;
import com.productpool.backend.dto.ConfigurationTypeQueryDTO;
import com.productpool.backend.dto.ConfigurationTypeUpdateDTO;
import com.productpool.backend.entity.ConfigurationType;
import com.productpool.backend.exception.BusinessLogicException;
import com.productpool.backend.exception.DuplicateResourceException;
import com.productpool.backend.exception.ResourceNotFoundException;
import com.productpool.backend.repository.BenchmarkRepository;
import com.productpool.backend.repository.ConfigurationTypeRepository;
import com.productpool.backend.service.ConfigurationTypeService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 配置类型服务实现类
 *
 * <p>实现配置类型的创建、查询、更新、删除及层级结构组装等业务逻辑。
 */
@Service
@RequiredArgsConstructor
public class ConfigurationTypeServiceImpl implements ConfigurationTypeService {

  private final ConfigurationTypeRepository configurationTypeRepository;
  private final BenchmarkRepository benchmarkRepository;

  /**
   * 创建配置类型
   *
   * <p>校验编码唯一性，验证父子分类关系后创建新配置类型。
   *
   * @param createDTO 创建配置类型的DTO
   * @return 创建后的配置类型DTO
   * @throws DuplicateResourceException 编码已存在时抛出
   * @throws ResourceNotFoundException 父分类不存在时抛出
   * @throws BusinessLogicException 分类关系不合法时抛出
   */
  @Override
  @Transactional
  public ConfigurationTypeDTO create(ConfigurationTypeCreateDTO createDTO) {
    // 检查name是否已存在
    if (configurationTypeRepository.existsByName(createDTO.getName())) {
      throw new DuplicateResourceException("ConfigurationType", "name", createDTO.getName());
    }

    // 大分类时parentId必须为空
    if (Boolean.TRUE.equals(createDTO.getIsMajor()) && createDTO.getParentId() != null) {
      throw new BusinessLogicException("Major type must not have a parent ID");
    }

    // 小分类时parentId必须不为空，且父分类必须是大分类
    if (Boolean.FALSE.equals(createDTO.getIsMajor())) {
      if (createDTO.getParentId() == null) {
        throw new BusinessLogicException("Sub-type must have a parent ID");
      }
      ConfigurationType parent =
          configurationTypeRepository
              .findById(createDTO.getParentId())
              .orElseThrow(
                  () ->
                      new ResourceNotFoundException("ConfigurationType", createDTO.getParentId()));
      if (Boolean.FALSE.equals(parent.getIsMajor())) {
        throw new BusinessLogicException("Parent configuration type must be a major type");
      }
    }

    ConfigurationType entity = new ConfigurationType();
    entity.setName(createDTO.getName());
    entity.setDescription(createDTO.getDescription());
    entity.setIsMajor(createDTO.getIsMajor());
    entity.setParentId(createDTO.getParentId());
    entity.setSortOrder(createDTO.getSortOrder());

    ConfigurationType saved = configurationTypeRepository.save(entity);
    return ConfigurationTypeDTO.fromEntity(saved);
  }

  /**
   * 根据ID查询配置类型
   *
   * @param id 配置类型ID
   * @return 配置类型DTO
   * @throws ResourceNotFoundException 配置类型不存在时抛出
   */
  @Override
  public ConfigurationTypeDTO findById(Long id) {
    ConfigurationType entity =
        configurationTypeRepository
            .findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("ConfigurationType", id));
    return ConfigurationTypeDTO.fromEntity(entity);
  }

  /**
   * 查询所有配置类型
   *
   * @return 配置类型DTO列表
   */
  @Override
  public List<ConfigurationTypeDTO> findAll() {
    List<ConfigurationType> entities = configurationTypeRepository.findAll();
    return entities.stream().map(ConfigurationTypeDTO::fromEntity).collect(Collectors.toList());
  }

  /**
   * 根据条件查询配置类型
   *
   * <p>支持按是否大分类、父分类ID、编码等条件查询。
   *
   * @param queryDTO 查询条件DTO
   * @return 配置类型DTO列表
   */
  @Override
  public List<ConfigurationTypeDTO> findByQuery(ConfigurationTypeQueryDTO queryDTO) {
    List<ConfigurationType> entities =
        configurationTypeRepository.findByQuery(
            queryDTO.getName(), queryDTO.getIsMajor(), queryDTO.getParentId());

    return entities.stream().map(ConfigurationTypeDTO::fromEntity).collect(Collectors.toList());
  }

  /**
   * 更新配置类型
   *
   * <p>仅更新非null字段（名称、描述、排序）。
   *
   * @param id 配置类型ID
   * @param updateDTO 更新配置类型的DTO
   * @return 更新后的配置类型DTO
   * @throws ResourceNotFoundException 配置类型不存在时抛出
   */
  @Override
  @Transactional
  public ConfigurationTypeDTO update(Long id, ConfigurationTypeUpdateDTO updateDTO) {
    ConfigurationType entity =
        configurationTypeRepository
            .findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("ConfigurationType", id));

    if (updateDTO.getName() != null) {
      entity.setName(updateDTO.getName());
    }
    if (updateDTO.getDescription() != null) {
      entity.setDescription(updateDTO.getDescription());
    }
    if (updateDTO.getSortOrder() != null) {
      entity.setSortOrder(updateDTO.getSortOrder());
    }

    ConfigurationType updated = configurationTypeRepository.save(entity);
    return ConfigurationTypeDTO.fromEntity(updated);
  }

  /**
   * 删除配置类型
   *
   * <p>删除前检查是否存在子分类和关联的业绩对标，存在则阻止删除。
   *
   * @param id 配置类型ID
   * @throws ResourceNotFoundException 配置类型不存在时抛出
   * @throws BusinessLogicException 存在子分类或关联业绩对标时抛出
   */
  @Override
  @Transactional
  public void delete(Long id) {
    ConfigurationType entity =
        configurationTypeRepository
            .findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("ConfigurationType", id));

    // 检查是否有子分类
    List<ConfigurationType> children =
        configurationTypeRepository.findByParentIdOrderBySortOrderAsc(id);
    if (!children.isEmpty()) {
      throw new BusinessLogicException(
          "Cannot delete configuration type with child types. Please delete child types first.");
    }

    // 检查是否有关联的业绩对标
    if (benchmarkRepository.existsByConfigurationTypeId(id)) {
      throw new BusinessLogicException(
          "Cannot delete configuration type with associated benchmarks. Please delete benchmarks first.");
    }

    configurationTypeRepository.delete(entity);
  }

  /**
   * 查询所有大分类及其子分类的层级结构
   *
   * @return 大分类列表，每个大分类包含其子分类
   */
  @Override
  public List<ConfigurationTypeDTO> findMajorTypesWithSubTypes() {
    List<ConfigurationType> majorTypes = configurationTypeRepository.findMajorTypesWithSubTypes();

    return majorTypes.stream()
        .map(
            majorType -> {
              ConfigurationTypeDTO dto = ConfigurationTypeDTO.fromEntity(majorType);

              // 获取子分类
              List<ConfigurationType> subTypes =
                  configurationTypeRepository.findByParentIdOrderBySortOrderAsc(majorType.getId());
              dto.setChildren(
                  subTypes.stream()
                      .map(ConfigurationTypeDTO::fromEntity)
                      .collect(Collectors.toList()));

              return dto;
            })
        .collect(Collectors.toList());
  }

  /**
   * 根据父分类ID查询子分类
   *
   * @param parentId 父分类ID
   * @return 子分类列表，按排序字段升序排列
   */
  @Override
  public List<ConfigurationTypeDTO> findByParentId(Long parentId) {
    List<ConfigurationType> entities =
        configurationTypeRepository.findByParentIdOrderBySortOrderAsc(parentId);
    return entities.stream().map(ConfigurationTypeDTO::fromEntity).collect(Collectors.toList());
  }

  /**
   * 查询所有大分类
   *
   * @return 大分类列表，按排序字段升序排列
   */
  @Override
  public List<ConfigurationTypeDTO> findMajorTypes() {
    List<ConfigurationType> entities =
        configurationTypeRepository.findByIsMajorTrueOrderBySortOrderAsc();
    return entities.stream().map(ConfigurationTypeDTO::fromEntity).collect(Collectors.toList());
  }
}
