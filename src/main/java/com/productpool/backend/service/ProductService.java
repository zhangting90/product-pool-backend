package com.productpool.backend.service;

import com.productpool.backend.dto.ProductCreateDTO;
import com.productpool.backend.dto.ProductDTO;
import com.productpool.backend.dto.ProductQueryDTO;
import com.productpool.backend.dto.ProductUpdateDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * 产品服务接口
 * <p>定义产品的增删改查、多条件分页查询及激活产品查询等业务方法。</p>
 */
public interface ProductService {

  /**
   * 创建产品
   *
   * @param createDTO 创建产品的DTO
   * @return 创建后的产品DTO
   */
  ProductDTO create(ProductCreateDTO createDTO);

  /**
   * 根据ID查询产品
   *
   * @param id 产品ID
   * @return 产品DTO
   */
  ProductDTO findById(Long id);

  /**
   * 查询所有产品（分页）
   *
   * @param pageable 分页参数
   * @return 产品分页结果
   */
  Page<ProductDTO> findAll(Pageable pageable);

  /**
   * 根据条件查询产品（分页）
   *
   * @param queryDTO 查询条件DTO
   * @param pageable 分页参数
   * @return 产品分页结果
   */
  Page<ProductDTO> findByQuery(ProductQueryDTO queryDTO, Pageable pageable);

  /**
   * 更新产品
   *
   * @param id 产品ID
   * @param updateDTO 更新产品的DTO
   * @return 更新后的产品DTO
   */
  ProductDTO update(Long id, ProductUpdateDTO updateDTO);

  /**
   * 删除产品
   *
   * @param id 产品ID
   */
  void delete(Long id);

  /**
   * 根据策略类型ID查询产品（分页）
   *
   * @param strategyTypeId 策略类型ID
   * @param pageable 分页参数
   * @return 产品分页结果
   */
  Page<ProductDTO> findByStrategyTypeId(Long strategyTypeId, Pageable pageable);

  /**
   * 查询所有激活的产品
   *
   * @param pageable 分页参数
   * @return 产品分页结果
   */
  Page<ProductDTO> findActiveProducts(Pageable pageable);
}
