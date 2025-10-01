package com.gaipov.talim_crm.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.gaipov.talim_crm.enums.PaymentStatus;
import com.gaipov.talim_crm.enums.PaymentType;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "payments")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class PaymentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JsonIgnoreProperties({"group", "payments"})
    private StudentEntity student;

    @ManyToOne
    @JsonIgnoreProperties({"students", "teacher", "payments"})
    private GroupEntity groupId;

    @Column
    private Double sum;

    @Enumerated(EnumType.STRING)
    private PaymentType paymentType;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    @Column
    private LocalDate createdAt;

    @Column
    private LocalDate deleted_at;
}
