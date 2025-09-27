package com.gaipov.talim_crm.service;

import com.gaipov.talim_crm.dto.StudentDto;
import com.gaipov.talim_crm.entity.GroupEntity;
import com.gaipov.talim_crm.entity.StudentEntity;
import com.gaipov.talim_crm.enums.UserRole;
import com.gaipov.talim_crm.enums.UserStatus;
import com.gaipov.talim_crm.exps.NotFoundExp;
import com.gaipov.talim_crm.repository.GroupRepo;
import com.gaipov.talim_crm.repository.ProfileRepository;
import com.gaipov.talim_crm.repository.StudentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StudentService {
    @Autowired
    StudentRepo studentRepo;

    @Autowired
    GroupRepo groupRepo;

    @Autowired
    ProfileRepository profileRepository;

    // Create new student and add to DB
    public StudentDto registerNewStudent(StudentDto dto) {
        StudentEntity entity = new StudentEntity();
        entity.setFullName(dto.getFullName());
        entity.setPhoneNum(dto.getPhoneNum());
        entity.setRoles(UserRole.STUDENT);
        entity.setStatus(UserStatus.ACTIVE);
        entity.setCreated_at(LocalDate.now());

        studentRepo.save(entity);
        return toDto(entity);
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

        return optional.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    // Sorting students by their status (Active/Block)
    public List<StudentDto> sortByStatus(UserStatus userStatus) {
        List<StudentEntity> optional = studentRepo.findByStatus(userStatus);

        if (optional.isEmpty()) {
            throw new NotFoundExp("Students with this status not found.");
        }

        return optional.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    // Add student to Group
    public String assignStudentToGroup(Long studentId, Long groupId) {
        StudentEntity studentEntity = studentRepo.findById(studentId)
                .orElseThrow(() -> new NotFoundExp("Student not found."));

        GroupEntity groupEntity = groupRepo.findById(groupId)
                .orElseThrow(() -> new NotFoundExp("Group is not found."));

        groupEntity.getListOfStudents().add(studentEntity);

        groupRepo.save(groupEntity);

        return "Successfully added.";
    }

    // remove student from group
    public String removeStudentFromGroup(Long studentId, Long groupId) {
        StudentEntity studentEntity = studentRepo.findById(studentId)
                .orElseThrow(() -> new NotFoundExp("Student not found."));

        GroupEntity groupEntity = groupRepo.findById(groupId)
                .orElseThrow(() -> new NotFoundExp("Group is not found."));

        groupEntity.getListOfStudents().remove(studentEntity);

        groupRepo.save(groupEntity);
        return "Successfully removed.";
    }


    // Use pagination
    public List<StudentDto> getAllStudentsByPagination(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return studentRepo.findAll(pageable).stream()
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

    public StudentDto updateStudent(StudentDto dto) {
        Optional<StudentEntity> studentOptional = studentRepo.findById(dto.getId());

        if (studentOptional.isPresent()) {
            StudentEntity existingStudent = studentOptional.get();

            existingStudent.setFullName(dto.getFullName());
            existingStudent.setPhoneNum(dto.getPhoneNum());
            existingStudent.setStatus(dto.getStatus());

            StudentEntity updatedStudent = studentRepo.save(existingStudent);

            return toDto(updatedStudent);
        } else {
            throw new RuntimeException("Ученик с ID " + dto.getId() + " не найден.");
        }
    }

    public Integer studentsCountWithActiveStatus(UserStatus status) {
        return studentRepo.findByStatus(status).size();
    }

    // Convert user to student and delete from auth table
    public StudentDto convertFromUser(Long userId, String fullName, String phoneNum) {
        StudentDto studentDto = new StudentDto();
        studentDto.setFullName(fullName);
        studentDto.setPhoneNum(phoneNum);
        studentDto.setStatus(UserStatus.IN_REGISTER);
        
        StudentDto createdStudent = registerNewStudent(studentDto);
        
        // Delete the user from auth table
        profileRepository.deleteById(userId);
        
        return createdStudent;
    }
}
