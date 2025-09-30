package com.gaipov.talim_crm.service;

import com.gaipov.talim_crm.dto.StatisticsDto;
import com.gaipov.talim_crm.entity.PaymentEntity;
import com.gaipov.talim_crm.entity.StudentEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface StatisticsService {
    StatisticsDto getOverallStatistics();

    Map<String, Object> getMonthlyStatistics(int year, int month);

    Map<String, Object> getChartData();

    List<StudentEntity> getRecentStudents(int limit);

    List<PaymentEntity> getRecentPayments(int limit);
}
