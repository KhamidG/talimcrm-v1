package com.gaipov.talim_crm.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gaipov.talim_crm.entity.StudentEntity;
import com.gaipov.talim_crm.enums.GroupRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupDto {
    private Long id;
    private String nameOfGroup;
    private String teacherFullName;
    private GroupRole typeOfGroup;
    @JsonIgnore
    private List<StudentEntity> listOfStudents; // TODO DTO bolishi kerak
    private Integer maxStudents;
    private Integer currentStudents;
    private LocalDate created_at;
    private String lessonStartTime;
    private String lessonEndTime;
    private String lessonDays;
    private Long teacherId;
}