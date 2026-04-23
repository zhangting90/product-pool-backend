package com.productpool.backend.entity;

import com.productpool.backend.util.IdGenerator;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 配置类型实体类
 * 对应数据库表 configuration_type，用于管理产品分类体系中的配置类型层级
 * 支持大分类和小分类的树形结构，通过 parentId 建立父子关系
 */
@Entity
@Table(name = "configuration_type")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConfigurationType {

  /** 主键ID */
  @Id
  @Column(name = "id", nullable = false, updatable = false)
  private Long id;

  /** 配置类型名称 */
  @Column(name = "name", nullable = false, length = 100)
  private String name;

  /** 配置类型编码，全局唯一 */
  @Column(name = "code", nullable = false, unique = true, length = 50)
  private String code;

  /** 配置类型描述 */
  @Column(name = "description", length = 500)
  private String description;

  /** 是否为大分类，true 表示大分类，false 表示小分类 */
  @Column(name = "is_major", nullable = false)
  private Boolean isMajor;

  /** 父分类ID，大分类时为null，小分类时指向所属大分类 */
  @Column(name = "parent_id")
  private Long parentId;

  /** 排序序号，数值越小越靠前 */
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
