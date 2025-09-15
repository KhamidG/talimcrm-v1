package com.gaipov.talim_crm.dto;

import com.gaipov.talim_crm.entity.GroupEntity;
import com.gaipov.talim_crm.enums.UserRole;
import com.gaipov.talim_crm.enums.UserStatus;
import lombok.Data;

import java.time.LocalDate;

@Data
public class StudentDto {
    private Long id;

    private String fullName;

    private String phoneNum;

    private GroupEntity group;

    private UserRole roles = UserRole.STUDENT;

    private UserStatus status;

    private LocalDate created_at;

    private LocalDate deleted_at;
}
