package com.gaipov.talim_crm.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class AttendanceDto {
    private Long id;
    private Long studentId;
    private String studentName;
    private Long groupId;
    private String groupName;
    private LocalDate attendanceDate;
    private Boolean isPresent;
    private String notes;
    private LocalDateTime createdAt;
    private Long createdByTeacherId;
}
