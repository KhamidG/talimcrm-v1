package com.gaipov.talim_crm.service;

import com.gaipov.talim_crm.dto.StudentDto;
import com.gaipov.talim_crm.entity.StudentEntity;
import com.gaipov.talim_crm.enums.UserRole;
import com.gaipov.talim_crm.enums.UserStatus;
import com.gaipov.talim_crm.exps.NotFoundExp;
import com.gaipov.talim_crm.repository.StudentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StudentService {
    @Autowired
    StudentRepo studentRepo;

    // Create new student and add to DB
    public StudentDto registerNewStudent(StudentDto dto) {
        StudentEntity entity = new StudentEntity();
        entity.setFullName(dto.getFullName());
        entity.setPhoneNum(dto.getPhoneNum());
        entity.setRoles(UserRole.STUDENT);
        entity.setStatus(UserStatus.ACTIVE);
        entity.setGroup(dto.getGroup());
        dto.setCreated_at(LocalDate.now());

        studentRepo.save(entity);
        dto.setId(entity.getId());

        return dto;
    }

    // All students list
    public List<StudentDto> getAllStudents() {
        return studentRepo.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    // Find student by id
    public Optional<StudentDto> getStudentById(Long id) {
        return studentRepo.findById(id).map(this::toDto);
    }

    // delete student by id
    public void deleteById(Long id) {
        StudentEntity entity = studentRepo.findById(id)
                .orElseThrow(() -> new NotFoundExp("Student not found"));

        entity.setDeleted_at(LocalDate.now());
        studentRepo.save(entity);
    }

    // find student by name/full name
    public List<StudentDto> findByFullName(String fullName) {
        List<StudentEntity> optional = studentRepo.findByFullName(fullName);

        if (optional.isEmpty()) {
            throw new NotFoundExp("Student not found.");
        }

        return studentRepo.findByFullName(fullName)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    // Sorting students by their status (Active/Block)
    public List<StudentDto> sortByStatus(UserStatus userStatus) {
        List<StudentEntity> optional = studentRepo.findByStatus(userStatus);

        if (optional.isEmpty()) {
            throw new NotFoundExp("Students with this status not found.");
        }

        return studentRepo.findByStatus(userStatus)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    // For convert entity to DTO
    private StudentDto toDto(StudentEntity entity) {
        StudentDto dto = new StudentDto();

        dto.setId(entity.getId());
        dto.setFullName(entity.getFullName());
        dto.setPhoneNum(entity.getPhoneNum());
        dto.setRoles(entity.getRoles());
        dto.setStatus(entity.getStatus());
        dto.setCreated_at(entity.getCreated_at());
        dto.setDeleted_at(entity.getDeleted_at());

        return dto;
    }

}
