package com.productpool.backend.entity;

import com.productpool.backend.util.IdGenerator;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** 策略类型实体类 对应数据库表 strategy_type，用于管理策略分类 关联到业绩对标，一个业绩对标下可有多个策略类型 */
@Entity
@Table(name = "strategy_type")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StrategyType {

  /** 主键ID */
  @Id
  @Column(name = "id", nullable = false, updatable = false)
  private Long id;

  /** 策略类型名称 */
  @Column(name = "name", nullable = false, length = 100)
  private String name;

  /** 策略类型描述 */
  @Column(name = "description", length = 500)
  private String description;

  /** 所属业绩对标ID，关联 benchmark 表 */
  @Column(name = "benchmark_id", nullable = false)
  private Long benchmarkId;

  /** 排序序号 */
  @Column(name = "sort_order")
  private Integer sortOrder;

  /** 创建时间 */
  @Column(name = "created_at", nullable = false, updatable = false)
  private LocalDateTime createdAt;

  /** 更新时间 */
  @Column(name = "updated_at")
  private LocalDateTime updatedAt;

  /** 持久化前回调：自动生成ID、设置时间戳和默认排序 */
  @PrePersist
  protected void onCreate() {
    if (id == null) {
      id = IdGenerator.generateId();
    }
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
