package com.gaipov.talim_crm.service;

import com.gaipov.talim_crm.dto.PaymentDto;
import com.gaipov.talim_crm.entity.GroupEntity;
import com.gaipov.talim_crm.entity.PaymentEntity;
import com.gaipov.talim_crm.entity.StudentEntity;
import com.gaipov.talim_crm.enums.PaymentStatus;
import com.gaipov.talim_crm.exps.NotFoundExp;
import com.gaipov.talim_crm.repository.GroupRepo;
import com.gaipov.talim_crm.repository.PaymentRepository;
import com.gaipov.talim_crm.repository.StudentRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final GroupRepo groupRepo;
    private final StudentRepo studentRepo;

    public PaymentDto createPayment(PaymentDto paymentDto, Long studentId, Long groupId) {
        StudentEntity studentEntity = studentRepo.findById(studentId)
                .orElseThrow(() -> new NotFoundExp("Student not found"));

        GroupEntity groupEntity = groupRepo.findById(groupId)
                .orElseThrow(() -> new NotFoundExp("Group not found"));

        PaymentEntity paymentEntity = new PaymentEntity();

        paymentEntity.setStudent(studentEntity);
        paymentEntity.setGroupId(groupEntity);

        paymentEntity.setSum(paymentDto.getSum());
        paymentEntity.setPaymentType(paymentDto.getPaymentType());
        paymentEntity.setPaymentStatus(PaymentStatus.NOT_PAID);
        paymentEntity.setCreatedAt(LocalDate.of(2025, 2, 1));

        PaymentEntity savedPayment = paymentRepository.save(paymentEntity);

        return toDto(savedPayment);
    }

    public List<PaymentDto> getOverduePayments() {
        LocalDate today = LocalDate.now();
        List<PaymentEntity> overduePayments = paymentRepository.findByCreatedAtBeforeAndPaymentStatusNot(today, PaymentStatus.PAID);

        return overduePayments.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public List<PaymentDto> listOfPayments() {
        return paymentRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public Optional<PaymentDto> getById(Long id) {
        return paymentRepository
                .findById(id)
                .map(this::toDto);
    }

    public String deletePayment(Long id) {
        PaymentEntity paymentEntity = paymentRepository.findById(id)
                .orElseThrow(() -> new NotFoundExp("Payment not found"));

        paymentEntity.setDeleted_at(LocalDate.now());
        paymentRepository.save(paymentEntity);

        return "Successfully removed.";
    }

    public String updatePayment(Long id, PaymentDto dto) {
        paymentRepository.findById(id)
                .orElseThrow(() -> new NotFoundExp("Payment not found."));

        return "null";
    }


    private PaymentDto toDto(PaymentEntity paymentEntity) {
        PaymentDto paymentDto = new PaymentDto();
        paymentDto.setId(paymentEntity.getId());
        paymentDto.setSum(paymentEntity.getSum());
        paymentDto.setPaymentType(paymentEntity.getPaymentType());
        paymentDto.setPaymentStatus(paymentEntity.getPaymentStatus());
        paymentDto.setCreated_at(paymentEntity.getCreatedAt());

        paymentDto.setStudent(paymentEntity.getStudent().getId());
        paymentDto.setGroupId(paymentEntity.getGroupId().getId());

        return paymentDto;
    }
}

