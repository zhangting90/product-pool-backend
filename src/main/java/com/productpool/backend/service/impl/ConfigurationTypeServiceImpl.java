package com.productpool.backend.service.impl;

import com.productpool.backend.dto.ConfigurationTypeCreateDTO;
import com.productpool.backend.dto.ConfigurationTypeDTO;
import com.productpool.backend.dto.ConfigurationTypeQueryDTO;
import com.productpool.backend.dto.ConfigurationTypeUpdateDTO;
import com.productpool.backend.entity.ConfigurationType;
import com.productpool.backend.exception.BusinessLogicException;
import com.productpool.backend.exception.DuplicateResourceException;
import com.productpool.backend.exception.ResourceNotFoundException;
import com.productpool.backend.repository.ConfigurationTypeRepository;
import com.productpool.backend.service.ConfigurationTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ConfigurationTypeServiceImpl implements ConfigurationTypeService {

    private final ConfigurationTypeRepository configurationTypeRepository;

    @Override
    @Transactional
    public ConfigurationTypeDTO create(ConfigurationTypeCreateDTO createDTO) {
        // 检查code是否已存在
        if (configurationTypeRepository.existsByCode(createDTO.getCode())) {
            throw new DuplicateResourceException(
                    "ConfigurationType", "code", createDTO.getCode());
        }

        // 如果是子分类，验证父分类是否存在
        if (Boolean.FALSE.equals(createDTO.getIsMajor()) && createDTO.getParentId() != null) {
            ConfigurationType parent = configurationTypeRepository
                    .findById(createDTO.getParentId())
                    .orElseThrow(() -> new ResourceNotFoundException("ConfigurationType", createDTO.getParentId()));

            // 父分类必须是大分类
            if (Boolean.FALSE.equals(parent.getIsMajor())) {
                throw new BusinessLogicException("Parent configuration type must be a major type");
            }
        } else if (Boolean.FALSE.equals(createDTO.getIsMajor()) && createDTO.getParentId() == null) {
            throw new BusinessLogicException("Sub-type must have a parent ID");
        }

        ConfigurationType entity = new ConfigurationType();
        entity.setName(createDTO.getName());
        entity.setCode(createDTO.getCode());
        entity.setDescription(createDTO.getDescription());
        entity.setIsMajor(createDTO.getIsMajor());
        entity.setParentId(createDTO.getParentId());
        entity.setSortOrder(createDTO.getSortOrder());

        ConfigurationType saved = configurationTypeRepository.save(entity);
        return ConfigurationTypeDTO.fromEntity(saved);
    }

    @Override
    public ConfigurationTypeDTO findById(Long id) {
        ConfigurationType entity = configurationTypeRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ConfigurationType", id));
        return ConfigurationTypeDTO.fromEntity(entity);
    }

    @Override
    public List<ConfigurationTypeDTO> findAll() {
        List<ConfigurationType> entities = configurationTypeRepository.findAll();
        return entities.stream()
                .map(ConfigurationTypeDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<ConfigurationTypeDTO> findByQuery(ConfigurationTypeQueryDTO queryDTO) {
        List<ConfigurationType> entities;

        if (queryDTO.getIsMajor() != null) {
            entities = configurationTypeRepository.findByIsMajorTrueOrderBySortOrderAsc();
        } else if (queryDTO.getParentId() != null) {
            entities = configurationTypeRepository.findByParentIdOrderBySortOrderAsc(queryDTO.getParentId());
        } else if (queryDTO.getCode() != null) {
            ConfigurationType entity = configurationTypeRepository.findByCode(queryDTO.getCode())
                    .orElseThrow(() -> new ResourceNotFoundException("ConfigurationType", queryDTO.getCode()));
            return List.of(ConfigurationTypeDTO.fromEntity(entity));
        } else {
            entities = configurationTypeRepository.findAll();
        }

        return entities.stream()
                .map(ConfigurationTypeDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ConfigurationTypeDTO update(Long id, ConfigurationTypeUpdateDTO updateDTO) {
        ConfigurationType entity = configurationTypeRepository
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

    @Override
    @Transactional
    public void delete(Long id) {
        ConfigurationType entity = configurationTypeRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ConfigurationType", id));

        // 检查是否有子分类
        List<ConfigurationType> children = configurationTypeRepository
                .findByParentIdOrderBySortOrderAsc(id);
        if (!children.isEmpty()) {
            throw new BusinessLogicException(
                    "Cannot delete configuration type with child types. Please delete child types first.");
        }

        // 检查是否有关联的业绩对标
        if (!entity.getBenchmarks().isEmpty()) {
            throw new BusinessLogicException(
                    "Cannot delete configuration type with associated benchmarks. Please delete benchmarks first.");
        }

        configurationTypeRepository.delete(entity);
    }

    @Override
    public List<ConfigurationTypeDTO> findMajorTypesWithSubTypes() {
        List<ConfigurationType> majorTypes = configurationTypeRepository.findMajorTypesWithSubTypes();

        return majorTypes.stream()
                .map(majorType -> {
                    ConfigurationTypeDTO dto = ConfigurationTypeDTO.fromEntity(majorType);

                    // 获取子分类
                    List<ConfigurationType> subTypes = configurationTypeRepository
                            .findByParentIdOrderBySortOrderAsc(majorType.getId());
                    dto.setChildren(
                            subTypes.stream()
                                    .map(ConfigurationTypeDTO::fromEntity)
                                    .collect(Collectors.toList()));

                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<ConfigurationTypeDTO> findByParentId(Long parentId) {
        List<ConfigurationType> entities = configurationTypeRepository
                .findByParentIdOrderBySortOrderAsc(parentId);
        return entities.stream()
                .map(ConfigurationTypeDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<ConfigurationTypeDTO> findMajorTypes() {
        List<ConfigurationType> entities = configurationTypeRepository.findByIsMajorTrueOrderBySortOrderAsc();
        return entities.stream()
                .map(ConfigurationTypeDTO::fromEntity)
                .collect(Collectors.toList());
    }
}
