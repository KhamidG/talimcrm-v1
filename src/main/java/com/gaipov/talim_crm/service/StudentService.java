package com.gaipov.talim_crm.service;

import com.gaipov.talim_crm.dto.StudentDto;
import com.gaipov.talim_crm.entity.StudentEntity;
import com.gaipov.talim_crm.enums.UserRole;
import com.gaipov.talim_crm.exps.NotFoundExp;
import com.gaipov.talim_crm.repository.StudentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class StudentService {
    //filter by name/lvl/group

    @Autowired
    StudentRepo studentRepo;

    // Create new student and add to DB
    public StudentDto registerNewStudent(StudentDto dto) {
        StudentEntity entity = new StudentEntity();
        entity.setFullName(dto.getFullName());
        entity.setPhoneNum(dto.getPhoneNum());
        entity.setRoles(UserRole.STUDENT);
        entity.setGroup(dto.getGroup());
        entity.setCreated_at(LocalDate.now());

        studentRepo.save(entity);
        dto.setId(entity.getId());

        return dto;
    }

    // Update students data
    public StudentDto updateStudentsInfo(Long id, StudentDto studentDto) {
        studentRepo.findById(id)
                .orElseThrow(() -> new NotFoundExp("Student not found."));
        throw new RuntimeException("dfs");
    }

}
