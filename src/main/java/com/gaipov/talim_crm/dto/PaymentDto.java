package com.gaipov.talim_crm.dto;

import com.gaipov.talim_crm.entity.GroupEntity;
import com.gaipov.talim_crm.entity.StudentEntity;
import com.gaipov.talim_crm.enums.PaymentStatus;
import com.gaipov.talim_crm.enums.PaymentType;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class PaymentDto {
    private Long id;
    private Long student;
    private Long groupId;
    private Double sum;
    private PaymentType paymentType;
    private PaymentStatus paymentStatus;
    private LocalDate created_at;
}
