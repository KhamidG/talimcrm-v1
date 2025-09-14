package com.gaipov.talim_crm.service;

import com.gaipov.talim_crm.dto.StudentDto;
import com.gaipov.talim_crm.dto.TeacherDto;
import com.gaipov.talim_crm.entity.StudentEntity;
import com.gaipov.talim_crm.entity.TeacherEntity;
import com.gaipov.talim_crm.enums.UserRole;
import com.gaipov.talim_crm.repository.StudentRepo;
import com.gaipov.talim_crm.repository.TeacherRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class AuthService {
    @Autowired
    StudentRepo studentRepo;

    @Autowired
    TeacherRepo teacherRepo;



    public TeacherDto createTeacher(TeacherDto dto) {
        TeacherEntity entity = new TeacherEntity();
        entity.setFullName(dto.getFullName());
        entity.setPhoneNum(dto.getPhoneNum());
        entity.setRoles(UserRole.TEACHER);
        entity.setLevelOfKnowledge(dto.getLevelOfKnowledge());
        entity.setCreated_at(LocalDate.now());

        teacherRepo.save(entity);
        dto.setId(entity.getId());

        return dto;
    }
}
