package com.productpool.backend.service.impl;

import com.productpool.backend.dto.BenchmarkCreateDTO;
import com.productpool.backend.dto.BenchmarkDTO;
import com.productpool.backend.dto.BenchmarkQueryDTO;
import com.productpool.backend.dto.BenchmarkUpdateDTO;
import com.productpool.backend.entity.Benchmark;
import com.productpool.backend.entity.ConfigurationType;
import com.productpool.backend.exception.BusinessLogicException;
import com.productpool.backend.exception.DuplicateResourceException;
import com.productpool.backend.exception.ResourceNotFoundException;
import com.productpool.backend.repository.BenchmarkRepository;
import com.productpool.backend.repository.ConfigurationTypeRepository;
import com.productpool.backend.service.BenchmarkService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BenchmarkServiceImpl implements BenchmarkService {

    private final BenchmarkRepository benchmarkRepository;
    private final ConfigurationTypeRepository configurationTypeRepository;

    @Override
    @Transactional
    public BenchmarkDTO create(BenchmarkCreateDTO createDTO) {
        // 检查code是否已存在
        if (benchmarkRepository.existsByCode(createDTO.getCode())) {
            throw new DuplicateResourceException(
                    "Benchmark", "code", createDTO.getCode());
        }

        // 验证配置类型是否存在
        ConfigurationType configurationType = configurationTypeRepository
                .findById(createDTO.getConfigurationTypeId())
                .orElseThrow(() -> new ResourceNotFoundException("ConfigurationType", createDTO.getConfigurationTypeId()));

        Benchmark entity = new Benchmark();
        entity.setName(createDTO.getName());
        entity.setCode(createDTO.getCode());
        entity.setDescription(createDTO.getDescription());
        entity.setConfigurationType(configurationType);
        entity.setSortOrder(createDTO.getSortOrder());

        Benchmark saved = benchmarkRepository.save(entity);
        return BenchmarkDTO.fromEntity(saved);
    }

    @Override
    public BenchmarkDTO findById(Long id) {
        Benchmark entity = benchmarkRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Benchmark", id));
        return BenchmarkDTO.fromEntity(entity);
    }

    @Override
    public List<BenchmarkDTO> findAll() {
        List<Benchmark> entities = benchmarkRepository.findAll();
        return entities.stream()
                .map(BenchmarkDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<BenchmarkDTO> findByQuery(BenchmarkQueryDTO queryDTO) {
        List<Benchmark> entities;

        if (queryDTO.getConfigurationTypeId() != null) {
            entities = benchmarkRepository
                    .findByConfigurationTypeIdOrderBySortOrderAsc(queryDTO.getConfigurationTypeId());
        } else if (queryDTO.getCode() != null) {
            Benchmark entity = benchmarkRepository.findByCode(queryDTO.getCode())
                    .orElseThrow(() -> new ResourceNotFoundException("Benchmark", queryDTO.getCode()));
            return List.of(BenchmarkDTO.fromEntity(entity));
        } else {
            entities = benchmarkRepository.findAll();
        }

        return entities.stream()
                .map(BenchmarkDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public BenchmarkDTO update(Long id, BenchmarkUpdateDTO updateDTO) {
        Benchmark entity = benchmarkRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Benchmark", id));

        if (updateDTO.getName() != null) {
            entity.setName(updateDTO.getName());
        }
        if (updateDTO.getDescription() != null) {
            entity.setDescription(updateDTO.getDescription());
        }
        if (updateDTO.getSortOrder() != null) {
            entity.setSortOrder(updateDTO.getSortOrder());
        }

        Benchmark updated = benchmarkRepository.save(entity);
        return BenchmarkDTO.fromEntity(updated);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Benchmark entity = benchmarkRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Benchmark", id));

        // 检查是否有关联的策略类型
        if (!entity.getStrategyTypes().isEmpty()) {
            throw new BusinessLogicException(
                    "Cannot delete benchmark with associated strategy types. Please delete strategy types first.");
        }

        benchmarkRepository.delete(entity);
    }

    @Override
    public List<BenchmarkDTO> findByConfigurationTypeId(Long configurationTypeId) {
        List<Benchmark> entities = benchmarkRepository
                .findByConfigurationTypeIdOrderBySortOrderAsc(configurationTypeId);
        return entities.stream()
                .map(BenchmarkDTO::fromEntity)
                .collect(Collectors.toList());
    }
}
