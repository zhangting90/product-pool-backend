package com.productpool.backend.service;

import com.productpool.backend.dto.BenchmarkCreateDTO;
import com.productpool.backend.dto.BenchmarkDTO;
import com.productpool.backend.dto.BenchmarkQueryDTO;
import com.productpool.backend.dto.BenchmarkUpdateDTO;
import java.util.List;

/**
 * 业绩对标服务接口
 *
 * <p>定义业绩对标的增删改查及按配置类型查询等业务方法。
 */
public interface BenchmarkService {

  /**
   * 创建业绩对标
   *
   * @param createDTO 创建业绩对标的DTO
   * @return 创建后的业绩对标DTO
   */
  BenchmarkDTO create(BenchmarkCreateDTO createDTO);

  /**
   * 根据ID查询业绩对标
   *
   * @param id 业绩对标ID
   * @return 业绩对标DTO
   */
  BenchmarkDTO findById(Long id);

  /**
   * 查询所有业绩对标
   *
   * @return 业绩对标DTO列表
   */
  List<BenchmarkDTO> findAll();

  /**
   * 根据条件查询业绩对标
   *
   * @param queryDTO 查询条件DTO
   * @return 业绩对标DTO列表
   */
  List<BenchmarkDTO> findByQuery(BenchmarkQueryDTO queryDTO);

  /**
   * 更新业绩对标
   *
   * @param id 业绩对标ID
   * @param updateDTO 更新业绩对标的DTO
   * @return 更新后的业绩对标DTO
   */
  BenchmarkDTO update(Long id, BenchmarkUpdateDTO updateDTO);

  /**
   * 删除业绩对标
   *
   * @param id 业绩对标ID
   */
  void delete(Long id);

  /**
   * 根据配置类型ID查询业绩对标
   *
   * @param configurationTypeId 配置类型ID
   * @return 业绩对标列表
   */
  List<BenchmarkDTO> findByConfigurationTypeId(Long configurationTypeId);
}
