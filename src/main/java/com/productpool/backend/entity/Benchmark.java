package com.productpool.backend.entity;

import com.productpool.backend.util.IdGenerator;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** 业绩对标实体类 对应数据库表 benchmark，用于管理业绩对标指标 关联到配置类型（小分类），一个配置类型下可有多个业绩对标 */
@Entity
@Table(name = "benchmark")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Benchmark {

  /** 主键ID */
  @Id
  @Column(name = "id", nullable = false, updatable = false)
  private Long id;

  /** 业绩对标名称，全局唯一 */
  @Column(name = "name", nullable = false, unique = true, length = 100)
  private String name;

  /** 业绩对标描述 */
  @Column(name = "description", length = 500)
  private String description;

  /** 所属配置类型ID，关联 configuration_type 表 */
  @Column(name = "configuration_type_id", nullable = false)
  private Long configurationTypeId;

  /** 排序序号 */
  @Column(name = "sort_order")
  private Integer sortOrder;

  /** 创建时间 */
  @Column(name = "created_at", nullable = false, updatable = false)
  private LocalDateTime createdAt;

  /** 更新时间 */
  @Column(name = "updated_at")
  private LocalDateTime updatedAt;

  /** 持久化前回调：自动生成ID、设置时间戳，sortOrder默认为创建时间戳（秒） */
  @PrePersist
  protected void onCreate() {
    if (id == null) {
      id = IdGenerator.generateId();
    }
    createdAt = LocalDateTime.now();
    updatedAt = LocalDateTime.now();
    if (sortOrder == null) {
      sortOrder = (int) createdAt.toEpochSecond(ZoneOffset.UTC);
    }
  }

  /** 更新前回调：自动刷新更新时间 */
  @PreUpdate
  protected void onUpdate() {
    updatedAt = LocalDateTime.now();
  }
}
