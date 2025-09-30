package com.gaipov.talim_crm.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class GradeDto {
    private Long id;
    private Long studentId;
    private String studentName;
    private Long groupId;
    private String groupName;
    private Double gradeValue;
    private LocalDate lessonDate;
    private String lessonTopic;
    private String comment;
    private LocalDateTime createdAt;
    private Long createdByTeacherId;
}
