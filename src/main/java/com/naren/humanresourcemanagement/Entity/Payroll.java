package com.naren.humanresourcemanagement.Entity;

import com.naren.humanresourcemanagement.Entity.Enums.PayrollStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "payroll")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Payroll {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @Column(name = "month", nullable = false)
    private Integer month;

    @Column(name = "year", nullable = false)
    private Integer year;

    @Column(name = "basic_salary", nullable = false, precision = 10, scale = 2)
    private Double basicSalary;

    @Column(name = "bonus", precision = 10, scale = 2)
    private Double bonus;

    @Column(name = "allowance", precision = 10, scale = 2)
    private Double allowance;

    @Column(name = "deduction", precision = 10, scale = 2)
    private Double deduction;

    @Column(name = "tax", precision = 10, scale = 2)
    private Double tax;

    @Column(name = "net_salary", nullable = false, precision = 10, scale = 2)
    private Double netSalary;

    @Column(name = "payment_date")
    private LocalDateTime paymentDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private PayrollStatus status;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;
}
