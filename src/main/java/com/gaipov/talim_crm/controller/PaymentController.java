package com.gaipov.talim_crm.controller;

import com.gaipov.talim_crm.dto.PaymentDto;
import com.gaipov.talim_crm.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/v1/pay")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping("/create/{studentId}/{groupId}")
    @ResponseBody
    private ResponseEntity<PaymentDto> createPayment(
            @RequestBody PaymentDto paymentDto,
            @PathVariable("studentId") Long studentId,
            @PathVariable("groupId") Long groupId) {
        return ResponseEntity.ok(paymentService.createPayment(paymentDto, studentId, groupId));
    }

    @GetMapping("/listPage")
    public String paymentsPage(Model model) {
        model.addAttribute("payments", paymentService.listOfPayments());
        return "payments";
    }

    @GetMapping("/statsPage")
    public String paymentsStatsPage(Model model) {
        model.addAttribute("payments", paymentService.listOfPayments());
        return "statistics";
    }

    @GetMapping("/monthlyStatsPage")
    public String monthlyStatsPage(Model model) {
        model.addAttribute("payments", paymentService.listOfPayments());
        return "monthly_payments";
    }

    @GetMapping("/createPage")
    public String createPaymentPage() {
        return "payments_create";
    }

    @GetMapping("/all")
    @ResponseBody
    private ResponseEntity<List<PaymentDto>> list() {
        return ResponseEntity.ok(paymentService.listOfPayments());
    }

    @GetMapping("/overduePayments")
    @ResponseBody
    private ResponseEntity<List<PaymentDto>> overduePaymentsList() {
        return ResponseEntity.ok(paymentService.getOverduePayments());
    }

    @GetMapping("/byId/{id}")
    @ResponseBody
    private ResponseEntity<Optional<PaymentDto>> getById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(paymentService.getById(id));
    }

    @DeleteMapping("/delete/{id}")
    @ResponseBody
    private ResponseEntity<String> deleteById(@PathVariable("id") Long id){
        return ResponseEntity.ok(paymentService.deletePayment(id));
    }

    @PutMapping("/update/{id}")
    @ResponseBody
    private ResponseEntity<String> update(@PathVariable("id") Long id, @RequestBody PaymentDto dto){
        return ResponseEntity.ok(paymentService.updatePayment(id, dto));
    }
}
