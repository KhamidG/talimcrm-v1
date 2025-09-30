package com.gaipov.talim_crm.repository;

import com.gaipov.talim_crm.entity.PaymentEntity;
import com.gaipov.talim_crm.enums.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<PaymentEntity, Long> {
    List<PaymentEntity> findByCreatedAtBeforeAndPaymentStatusNot(LocalDate date, PaymentStatus paymentStatus);

}
