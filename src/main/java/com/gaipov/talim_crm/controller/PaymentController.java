package com.gaipov.talim_crm.controller;

import com.gaipov.talim_crm.dto.PaymentDto;
import com.gaipov.talim_crm.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/v1/pay")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping("/create/{studentId}/{groupId}")
    private ResponseEntity<PaymentDto> createPayment(
            @RequestBody PaymentDto paymentDto,
            @PathVariable("studentId") Long studentId,
            @PathVariable("groupId") Long groupId) {
        return ResponseEntity.ok(paymentService.createPayment(paymentDto, studentId, groupId));
    }

    @GetMapping("/all")
    private ResponseEntity<List<PaymentDto>> list() {
        return ResponseEntity.ok(paymentService.listOfPayments());
    }

    @GetMapping("/overduePayments")
    private ResponseEntity<List<PaymentDto>> overduePaymentsList() {
        return ResponseEntity.ok(paymentService.getOverduePayments());
    }

    @GetMapping("/byId/{id}")
    private ResponseEntity<Optional<PaymentDto>> getById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(paymentService.getById(id));
    }

    @DeleteMapping("/delete/{id}")
    private ResponseEntity<String> deleteById(@PathVariable("id") Long id){
        return ResponseEntity.ok(paymentService.deletePayment(id));
    }

    @PutMapping("/update/{id}")
    private ResponseEntity<String> update(@PathVariable("id") Long id, @RequestBody PaymentDto dto){
        return ResponseEntity.ok(paymentService.updatePayment(id, dto));
    }


}
