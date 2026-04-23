package com.productpool.backend.service;

import com.productpool.backend.vo.ProductPoolVO;
import java.util.List;

/**
 * 产品池服务接口
 *
 * <p>定义获取完整产品池层级数据的业务方法， 层级结构为：大分类 -> 小分类 -> 业绩对标 -> 策略类型 -> 产品。
 */
public interface ProductPoolService {

  /**
   * 获取完整的产品池数据 包含所有层级的完整结构：大分类 → 小分类 → 业绩对标 → 策略类型 → 产品
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
