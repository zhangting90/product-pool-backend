package com.productpool.backend.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** 产品实体类 对应数据库表 product，用于管理产品信息 关联到策略类型，一个策略类型下可有多个产品 */
@Entity
@Table(name = "product")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {

  /** 产品编码，主键 */
  @Id
  @Column(name = "code", nullable = false, length = 50)
  private String code;

  /** 产品名称 */
  @Column(name = "name", nullable = false, length = 200)
  private String name;

  /** 所属策略类型ID，关联 strategy_type 表 */
  @Column(name = "strategy_type_id", nullable = false)
  private Long strategyTypeId;

  /** 产品描述 */
  @Column(name = "description", length = 1000)
  private String description;

  /** 排序序号 */
  @Column(name = "sort_order")
  private Integer sortOrder;

  /** 创建时间 */
  @Column(name = "created_at", nullable = false, updatable = false)
  private LocalDateTime createdAt;

  /** 更新时间 */
  @Column(name = "updated_at")
  private LocalDateTime updatedAt;

  /** 持久化前回调：设置时间戳，sortOrder默认为0 */
  @PrePersist
  protected void onCreate() {
    createdAt = LocalDateTime.now();
    updatedAt = LocalDateTime.now();
    if (sortOrder == null) {
      sortOrder = 0;
    }
  }

  /** 更新前回调：自动刷新更新时间 */
  @PreUpdate
  protected void onUpdate() {
    updatedAt = LocalDateTime.now();
  }
}
