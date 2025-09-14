package com.gaipov.talim_crm.dto;

import com.gaipov.talim_crm.entity.GroupEntity;
import com.gaipov.talim_crm.enums.UserRole;
import lombok.Data;

import java.time.LocalDate;

@Data
public class StudentDto {
    private Long id;

    private String fullName;

    private String phoneNum;

    private GroupEntity group;

    private UserRole roles = UserRole.STUDENT;

    private LocalDate created_at;
}
