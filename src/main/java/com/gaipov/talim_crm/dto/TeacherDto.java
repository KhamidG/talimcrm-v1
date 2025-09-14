package com.gaipov.talim_crm.dto;

import com.gaipov.talim_crm.entity.GroupEntity;
import com.gaipov.talim_crm.enums.UserRole;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class TeacherDto {
    private Long id;

    private String fullName;

    private String phoneNum;

    private UserRole roles = UserRole.TEACHER;

    private String levelOfKnowledge;

    private List<GroupEntity> groups;

    private LocalDate created_at;
}
