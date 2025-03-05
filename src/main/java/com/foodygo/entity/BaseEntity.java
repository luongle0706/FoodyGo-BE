package com.foodygo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@MappedSuperclass
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BaseEntity {

    @Column(name = "created_at", columnDefinition = "DATETIME(0)")
    @CreationTimestamp
    LocalDateTime createdAt;

    @Column(name = "updated_at", columnDefinition = "DATETIME(0)")
    @UpdateTimestamp
    LocalDateTime updatedAt;

    @Column(columnDefinition = "TINYINT(1) DEFAULT 0")
    boolean deleted = false;
}
