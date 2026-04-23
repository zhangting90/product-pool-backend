package com.productpool.backend.entity;

import com.productpool.backend.util.IdGenerator;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "configuration_type")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConfigurationType {

  @Id
  @Column(name = "id", nullable = false, updatable = false)
  private Long id;

  @Column(name = "name", nullable = false, length = 100)
  private String name;

  @Column(name = "code", nullable = false, unique = true, length = 50)
  private String code;

  @Column(name = "description", length = 500)
  private String description;

  @Column(name = "is_major", nullable = false)
  private Boolean isMajor;

  @Column(name = "parent_id")
  private Long parentId;

  @Column(name = "sort_order")
  private Integer sortOrder;

  @Column(name = "created_at", nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @Column(name = "updated_at")
  private LocalDateTime updatedAt;

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

  @PreUpdate
  protected void onUpdate() {
    updatedAt = LocalDateTime.now();
  }
}
