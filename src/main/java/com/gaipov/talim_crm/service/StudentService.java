package com.gaipov.talim_crm.service;

import com.gaipov.talim_crm.dto.StudentDto;
import com.gaipov.talim_crm.enums.UserStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface StudentService {
    StudentDto registerNewStudent(StudentDto dto);

    List<StudentDto> getAllStudents();

    Optional<StudentDto> getStudentById(Long id);

    void deleteById(Long id);

    List<StudentDto> findByFullName(String fullName);

    List<StudentDto> sortByStatus(UserStatus userStatus);

    String assignStudentToGroup(Long studentId, Long groupId);

    String removeStudentFromGroup(Long studentId, Long groupId);

    StudentDto updateStudent(StudentDto dto);

    Integer studentsCountWithActiveStatus(UserStatus status);

    StudentDto convertFromUser(Long userId, String fullName, String phoneNum);
}
