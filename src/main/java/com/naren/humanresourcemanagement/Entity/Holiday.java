package com.naren.humanresourcemanagement.Entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "holiday")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Holiday {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "holiday_date", nullable = false)
    private LocalDateTime holidayDate;

    @Column(name = "description", length = 500)
    private String description;

    @Column(name = "is_optional", nullable = false)
    private Boolean isOptional;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;
}
