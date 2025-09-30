package com.gaipov.talim_crm.service;

import com.gaipov.talim_crm.dto.PaymentDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface PaymentService {

    PaymentDto createPayment(PaymentDto paymentDto, Long studentId, Long groupId);

    List<PaymentDto> getOverduePayments();

    String updatePayment(Long id);

    String deletePayment(Long id);

    Optional<PaymentDto> getById(Long id);

    List<PaymentDto> listOfPayments();
}
