package com.productpool.backend.service;

import com.productpool.backend.dto.ProductCreateDTO;
import com.productpool.backend.dto.ProductDTO;
import com.productpool.backend.dto.ProductQueryDTO;
import com.productpool.backend.dto.ProductUpdateDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * 产品服务接口
 *
 * <p>定义产品的增删改查及多条件分页查询等业务方法。
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
   * 根据编码查询产品
   *
   * @param code 产品编码
   * @return 产品DTO
   */
  ProductDTO findById(String code);

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
   * @param code 产品编码
   * @param updateDTO 更新产品的DTO
   * @return 更新后的产品DTO
   */
  ProductDTO update(String code, ProductUpdateDTO updateDTO);

  /**
   * 删除产品
   *
   * @param code 产品编码
   */
  void delete(String code);

  /**
   * 根据策略类型ID查询产品（分页）
   *
   * @param strategyTypeId 策略类型ID
   * @param pageable 分页参数
   * @return 产品分页结果
   */
  Page<ProductDTO> findByStrategyTypeId(Long strategyTypeId, Pageable pageable);
}
