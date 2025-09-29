package com.gaipov.talim_crm.service;

import com.gaipov.talim_crm.dto.PaymentDto;
import com.gaipov.talim_crm.dto.StatisticsDto;
import com.gaipov.talim_crm.dto.StudentDto;
import com.gaipov.talim_crm.entity.AttendanceEntity;
import com.gaipov.talim_crm.entity.GroupEntity;
import com.gaipov.talim_crm.entity.PaymentEntity;
import com.gaipov.talim_crm.entity.StudentEntity;
import com.gaipov.talim_crm.entity.TeacherEntity;
import com.gaipov.talim_crm.enums.PaymentStatus;
import com.gaipov.talim_crm.enums.PaymentType;
import com.gaipov.talim_crm.enums.UserStatus;
import com.gaipov.talim_crm.repository.AttendanceRepository;
import com.gaipov.talim_crm.repository.GroupRepo;
import com.gaipov.talim_crm.repository.PaymentRepository;
import com.gaipov.talim_crm.repository.ProfileRepository;
import com.gaipov.talim_crm.repository.StudentRepo;
import com.gaipov.talim_crm.repository.TeacherRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.Year;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.math.BigDecimal.*;

@Service
@RequiredArgsConstructor
public class StatisticsService {
    
    private final StudentRepo studentRepo;
    private final GroupRepo groupRepo;
    private final TeacherRepo teacherRepo;
    private final PaymentRepository paymentRepository;
    private final ProfileRepository profileRepository;
    private final AttendanceRepository attendanceRepository;

    public StatisticsDto getOverallStatistics() {
        StatisticsDto stats = new StatisticsDto();
        
        // Student statistics
        List<StudentEntity> allStudents = studentRepo.findAll();
        stats.setTotalStudents(allStudents.size());
        stats.setActiveStudents((int) allStudents.stream().filter(s -> s.getStatus() == UserStatus.ACTIVE).count());
        stats.setStoppedStudents((int) allStudents.stream().filter(s -> s.getStatus() == UserStatus.STOP_LEARNING).count());
        stats.setInRegisterStudents((int) allStudents.stream().filter(s -> s.getStatus() == UserStatus.IN_REGISTER).count());
        
        // Group statistics
        List<GroupEntity> allGroups = groupRepo.findAll();
        stats.setTotalGroups(allGroups.size());
        stats.setActiveGroups((int) allGroups.stream().filter(g -> g.getDeleted_at() == null).count());
        
        // Teacher statistics
        List<TeacherEntity> allTeachers = teacherRepo.findAll();
        stats.setTotalTeachers(allTeachers.size());
        stats.setActiveTeachers((int) allTeachers.stream().filter(t -> t.getDeleted_at() == null).count());
        
        // Payment statistics
        List<PaymentEntity> allPayments = paymentRepository.findAll();
        double totalRevenue = allPayments.stream()
                .filter(p -> p.getPaymentStatus() == PaymentStatus.COMPLETED || p.getPaymentStatus() == PaymentStatus.PAID)
                .mapToDouble(p -> p.getSum() != null ? p.getSum() : 0.0)
                .sum();
        stats.setTotalRevenue(totalRevenue);
        
        // Monthly revenue (current month)
        LocalDate now = LocalDate.now();
        double monthlyRevenue = allPayments.stream()
                .filter(p -> p.getPaymentStatus() == PaymentStatus.COMPLETED || p.getPaymentStatus() == PaymentStatus.PAID)
                .filter(p -> p.getCreatedAt() != null &&
                           p.getCreatedAt().getMonth() == now.getMonth() &&
                           p.getCreatedAt().getYear() == now.getYear())
                .mapToDouble(p -> p.getSum() != null ? p.getSum() : 0.0)
                .sum();
        stats.setMonthlyRevenue(monthlyRevenue);
        
        // Reception clients
        long receptionClients = profileRepository.findAll().stream()
                .filter(a -> a.getStatus() == UserStatus.IN_REGISTER)
                .count();
        stats.setReceptionClients((int) receptionClients);
        
        // New students this month
        long newThisMonth = allStudents.stream()
                .filter(s -> s.getCreated_at() != null && 
                           s.getCreated_at().getMonth() == now.getMonth() && 
                           s.getCreated_at().getYear() == now.getYear())
                .count();
        stats.setNewStudentsThisMonth((int) newThisMonth);
        
        // Stopped students this month
        long stoppedThisMonth = allStudents.stream()
                .filter(s -> s.getStatus() == UserStatus.STOP_LEARNING)
                .filter(s -> s.getDeleted_at() != null && 
                           s.getDeleted_at().getMonth() == now.getMonth() && 
                           s.getDeleted_at().getYear() == now.getYear())
                .count();
        stats.setStoppedStudentsThisMonth((int) stoppedThisMonth);
        
        return stats;
    }

    public Map<String, Object> getMonthlyStatistics(int year, int month) {
        Map<String, Object> monthlyStats = new HashMap<>();
        
        LocalDate startDate = LocalDate.of(year, month + 1, 1);
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());
        
        // Students registered this month
        List<StudentEntity> newStudents = studentRepo.findAll().stream()
                .filter(s -> s.getCreated_at() != null && 
                           s.getCreated_at().getMonth() == Month.of(month + 1) && 
                           s.getCreated_at().getYear() == year)
                .toList();
        monthlyStats.put("newStudents", newStudents.size());
        
        // Students stopped this month
        long stoppedStudents = studentRepo.findAll().stream()
                .filter(s -> s.getStatus() == UserStatus.STOP_LEARNING)
                .filter(s -> s.getDeleted_at() != null && 
                           s.getDeleted_at().getMonth() == Month.of(month + 1) && 
                           s.getDeleted_at().getYear() == year)
                .count();
        monthlyStats.put("stoppedStudents", stoppedStudents);
        
        // Revenue this month
        double monthlyRevenue = paymentRepository.findAll().stream()
                .filter(p -> p.getPaymentStatus() == PaymentStatus.COMPLETED)
                .filter(p -> p.getCreatedAt() != null &&
                           p.getCreatedAt().getMonth() == Month.of(month + 1) &&
                           p.getCreatedAt().getYear() == year)
                .mapToDouble(p -> p.getSum() != null ? p.getSum() : 0.0)
                .sum();
        monthlyStats.put("monthlyRevenue", monthlyRevenue);
        
        // Payment types distribution
        Map<PaymentType, Long> paymentTypes = new HashMap<>();
        paymentRepository.findAll().stream()
                .filter(p -> p.getCreatedAt() != null &&
                           p.getCreatedAt().getMonth() == Month.of(month + 1) &&
                           p.getCreatedAt().getYear() == year)
                .forEach(p -> {
                    PaymentType type = p.getPaymentType();
                    paymentTypes.put(type, paymentTypes.getOrDefault(type, 0L) + 1);
                });
        monthlyStats.put("paymentTypes", paymentTypes);
        
        return monthlyStats;
    }

    public Map<String, Object> getChartData() {
        Map<String, Object> chartData = new HashMap<>();
        
        // Student status distribution
        List<StudentEntity> allStudents = studentRepo.findAll();
        Map<String, Long> statusDistribution = new HashMap<>();
        statusDistribution.put("ACTIVE", allStudents.stream().filter(s -> s.getStatus() == UserStatus.ACTIVE).count());
        statusDistribution.put("STOP_LEARNING", allStudents.stream().filter(s -> s.getStatus() == UserStatus.STOP_LEARNING).count());
        statusDistribution.put("IN_REGISTER", allStudents.stream().filter(s -> s.getStatus() == UserStatus.IN_REGISTER).count());
        chartData.put("studentStatus", statusDistribution);
        
        // Monthly registrations (last 12 months)
        Map<String, Long> monthlyRegistrations = new HashMap<>();
        LocalDate now = LocalDate.now();
        for (int i = 11; i >= 0; i--) {
            LocalDate monthDate = now.minusMonths(i);
            long count = allStudents.stream()
                    .filter(s -> s.getCreated_at() != null && 
                               s.getCreated_at().getMonth() == monthDate.getMonth() && 
                               s.getCreated_at().getYear() == monthDate.getYear())
                    .count();
            monthlyRegistrations.put(monthDate.getMonth().name().substring(0, 3), count);
        }
        chartData.put("monthlyRegistrations", monthlyRegistrations);
        
        // Payment types distribution
        List<PaymentEntity> allPayments = paymentRepository.findAll();
        Map<String, Long> paymentTypeDistribution = new HashMap<>();
        allPayments.forEach(p -> {
            String type = p.getPaymentType() != null ? p.getPaymentType().name() : "UNKNOWN";
            paymentTypeDistribution.put(type, paymentTypeDistribution.getOrDefault(type, 0L) + 1);
        });
        chartData.put("paymentTypes", paymentTypeDistribution);
        
        // Monthly revenue (last 12 months)
        Map<String, Double> monthlyRevenue = new HashMap<>();
        for (int i = 11; i >= 0; i--) {
            LocalDate monthDate = now.minusMonths(i);
            double revenue = allPayments.stream()
                    .filter(p -> p.getPaymentStatus() == PaymentStatus.COMPLETED)
                    .filter(p -> p.getCreatedAt() != null &&
                               p.getCreatedAt().getMonth() == monthDate.getMonth() &&
                               p.getCreatedAt().getYear() == monthDate.getYear())
                    .mapToDouble(p -> p.getSum() != null ? p.getSum() : 0.0)
                    .sum();
            monthlyRevenue.put(monthDate.getMonth().name().substring(0, 3), revenue);
        }
        chartData.put("monthlyRevenue", monthlyRevenue);
        
        return chartData;
    }

    public List<StudentDto> getRecentStudents(int limit) {
        return studentRepo.findAll().stream()
                .sorted((a, b) -> {
                    if (a.getCreated_at() == null && b.getCreated_at() == null) return 0;
                    if (a.getCreated_at() == null) return 1;
                    if (b.getCreated_at() == null) return -1;
                    return b.getCreated_at().compareTo(a.getCreated_at());
                })
                .limit(limit)
                .map(se -> {
                    StudentDto dto = new StudentDto();
                    dto.setId(se.getId());
                    dto.setFullName(se.getFullName());
                    dto.setPhoneNum(se.getPhoneNum());
                    dto.setStatus(se.getStatus());
                    dto.setCreated_at(se.getCreated_at());
                    return dto;
                })
                .toList();
    }

    public List<PaymentDto> getRecentPayments(int limit) {
        return paymentRepository.findAll().stream()
                .sorted((a, b) -> {
                    if (a.getCreatedAt() == null && b.getCreatedAt() == null) return 0;
                    if (a.getCreatedAt() == null) return 1;
                    if (b.getCreatedAt() == null) return -1;
                    return b.getCreatedAt().compareTo(a.getCreatedAt());
                })
                .limit(limit)
                .map(pe -> {
                    PaymentDto dto = new PaymentDto();
                    dto.setId(pe.getId());
                    dto.setSum(pe.getSum());
                    dto.setPaymentType(pe.getPaymentType());
                    dto.setPaymentStatus(pe.getPaymentStatus());
                    dto.setCreated_at(pe.getCreatedAt());
                    return dto;
                })
                .toList();
    }
}
