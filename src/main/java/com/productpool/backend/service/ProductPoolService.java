package com.productpool.backend.service;

import com.productpool.backend.vo.ProductPoolVO;

import java.util.List;

public interface ProductPoolService {

  /**
   * 获取完整的产品池数据
   * 包含所有层级的完整结构：大分类 → 小分类 → 业绩对标 → 策略类型 → 产品
   *
   * @return 产品池视图对象列表
   */
  List<ProductPoolVO> getProductPoolData();

  /**
   * 获取产品池数据（仅激活的产品）
   *
   * @return 产品池视图对象列表
   */
  List<ProductPoolVO> getActiveProductPoolData();
}
