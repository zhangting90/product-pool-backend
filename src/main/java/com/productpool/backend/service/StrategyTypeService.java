package com.productpool.backend.service;

import com.productpool.backend.dto.StrategyTypeCreateDTO;
import com.productpool.backend.dto.StrategyTypeDTO;
import com.productpool.backend.dto.StrategyTypeQueryDTO;
import com.productpool.backend.dto.StrategyTypeUpdateDTO;
import java.util.List;

/**
 * 策略类型服务接口
 *
 * <p>定义策略类型的增删改查及按业绩对标查询等业务方法。
 */
public interface StrategyTypeService {

  /**
   * 创建策略类型
   *
   * @param createDTO 创建策略类型的DTO
   * @return 创建后的策略类型DTO
   */
  StrategyTypeDTO create(StrategyTypeCreateDTO createDTO);

  /**
   * 根据ID查询策略类型
   *
   * @param id 策略类型ID
   * @return 策略类型DTO
   */
  StrategyTypeDTO findById(Long id);

  /**
   * 查询所有策略类型
   *
   * @return 策略类型DTO列表
   */
  List<StrategyTypeDTO> findAll();

  /**
   * 根据条件查询策略类型
   *
   * @param queryDTO 查询条件DTO
   * @return 策略类型DTO列表
   */
  List<StrategyTypeDTO> findByQuery(StrategyTypeQueryDTO queryDTO);

  /**
   * 更新策略类型
   *
   * @param id 策略类型ID
   * @param updateDTO 更新策略类型的DTO
   * @return 更新后的策略类型DTO
   */
  StrategyTypeDTO update(Long id, StrategyTypeUpdateDTO updateDTO);

  /**
   * 删除策略类型
   *
   * @param id 策略类型ID
   */
  void delete(Long id);

  /**
   * 根据业绩对标ID查询策略类型
   *
   * @param benchmarkId 业绩对标ID
   * @return 策略类型列表
   */
  List<StrategyTypeDTO> findByBenchmarkId(Long benchmarkId);
}
