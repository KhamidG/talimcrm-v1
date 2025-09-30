package com.gaipov.talim_crm.controller;

import com.gaipov.talim_crm.dto.StatisticsDto;
import com.gaipov.talim_crm.entity.PaymentEntity;
import com.gaipov.talim_crm.entity.StudentEntity;
import com.gaipov.talim_crm.service.Impl.StatisticsServiceImpl;
import com.gaipov.talim_crm.service.StatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/v1/stats")
@RequiredArgsConstructor
public class StatisticsController {
    
    private final StatisticsService statisticsService;

    @GetMapping("/page")
    public String showStatisticsPage(Model model) {
        StatisticsDto stats = statisticsService.getOverallStatistics();
        model.addAttribute("statistics", stats);
        return "statistics";
    }

    @GetMapping("/overview")
    @ResponseBody
    public ResponseEntity<StatisticsDto> getOverallStatistics() {
        StatisticsDto stats = statisticsService.getOverallStatistics();
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/monthly/{year}/{month}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getMonthlyStatistics(
            @PathVariable int year, 
            @PathVariable int month) {
        Map<String, Object> monthlyStats = statisticsService.getMonthlyStatistics(year, month);
        return ResponseEntity.ok(monthlyStats);
    }

    @GetMapping("/charts")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getChartData() {
        Map<String, Object> chartData = statisticsService.getChartData();
        return ResponseEntity.ok(chartData);
    }

    @GetMapping("/recent/students")
    @ResponseBody
    public ResponseEntity<List<StudentEntity>> getRecentStudents(@RequestParam(defaultValue = "5") int limit) {
        List<StudentEntity> recentStudents = statisticsService.getRecentStudents(limit);
        return ResponseEntity.ok(recentStudents);
    }

    @GetMapping("/recent/payments")
    @ResponseBody
    public ResponseEntity<List<PaymentEntity>> getRecentPayments(@RequestParam(defaultValue = "5") int limit) {
        List<PaymentEntity> recentPayments = statisticsService.getRecentPayments(limit);
        return ResponseEntity.ok(recentPayments);
    }
}
