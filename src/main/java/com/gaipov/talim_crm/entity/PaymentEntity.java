package com.gaipov.talim_crm.entity;

import com.gaipov.talim_crm.enums.PaymentStatus;
import com.gaipov.talim_crm.enums.PaymentType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Generated;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "payments")
public class PaymentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private StudentEntity student;

    @ManyToOne
    private GroupEntity groupId;

    @Column
    private BigDecimal sum;

    @Enumerated(EnumType.STRING)
    private PaymentType paymentType;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    @Column
    private LocalDate createdAt;

    @Column
    private LocalDate deleted_at;
}
