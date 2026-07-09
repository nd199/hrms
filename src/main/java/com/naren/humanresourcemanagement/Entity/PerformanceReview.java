package com.naren.humanresourcemanagement.Entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "performance_review")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class PerformanceReview {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reviewer_id", nullable = false)
    private Employee reviewer;

    @Column(name = "review_period", length = 50)
    private String reviewPeriod;

    @Column(name = "rating", nullable = false)
    private Integer rating;

    @Column(name = "strengths", length = 1000)
    private String strengths;

    @Column(name = "weaknesses", length = 1000)
    private String weaknesses;

    @Column(name = "goals", length = 1000)
    private String goals;

    @Column(name = "comments", length = 1000)
    private String comments;

    @Column(name = "review_date", nullable = false)
    private LocalDateTime reviewDate;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;
}
