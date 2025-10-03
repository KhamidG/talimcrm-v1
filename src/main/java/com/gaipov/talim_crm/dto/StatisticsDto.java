package com.gaipov.talim_crm.dto;

import lombok.Data;

@Data
public class StatisticsDto {
    // Student statistics
    private int totalStudents;
    private int activeStudents;
    private int stoppedStudents;
    private int inRegisterStudents;
    private int newStudentsThisMonth;
    private int stoppedStudentsThisMonth;
    private long debtorsCount;
    
    // Group statistics
    private int totalGroups;
    private int activeGroups;
    
    // Teacher statistics
    private int totalTeachers;
    private int activeTeachers;
    
    // Payment statistics
    private double totalRevenue;
    private double monthlyRevenue;
    
    // Reception statistics
    private int receptionClients;
}
