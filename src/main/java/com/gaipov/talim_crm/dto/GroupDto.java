package com.gaipov.talim_crm.dto;

import com.gaipov.talim_crm.entity.StudentEntity;
import com.gaipov.talim_crm.enums.GroupRole;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class GroupDto {
    private Long id;
    private String nameOfGroup;
    private String teacherFullName;
    private GroupRole typeOfGroup;
    private List<StudentEntity> listOfStudents;
    private Integer maxStudents;
    private Integer currentStudents;
    private LocalDate created_at;
}