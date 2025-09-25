package com.gaipov.talim_crm.service;

import com.gaipov.talim_crm.dto.StudentDto;
import com.gaipov.talim_crm.dto.TeacherDto;
import com.gaipov.talim_crm.entity.TeacherEntity;
import com.gaipov.talim_crm.enums.UserRole;
import com.gaipov.talim_crm.exps.NotFoundExp;
import com.gaipov.talim_crm.repository.TeacherRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TeacherService {
    private final TeacherRepo teacherRepo;

    public TeacherDto createTeacher(TeacherDto teacherDto) {
        TeacherEntity teacherEntity = new TeacherEntity();
        teacherEntity.setFullName(teacherDto.getFullName());
        teacherEntity.setPhoneNum(teacherDto.getPhoneNum());
        teacherEntity.setLevelOfKnowledge(teacherDto.getLevelOfKnowledge());
        teacherEntity.setRoles(UserRole.TEACHER);
        teacherEntity.setCreated_at(LocalDate.now());

        teacherRepo.save(teacherEntity);
        teacherDto.setId(teacherEntity.getId());

        return teacherDto;
    }

    public List<TeacherDto> listOfDto() {
        return teacherRepo.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public List<TeacherDto> findByNameOfTeacher(String name) {
        List<TeacherEntity> optional = teacherRepo.findByFullName(name);

        if (optional.isEmpty()) {
            throw new NotFoundExp("Teacher not found");
        }
        return optional.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    // All teachers list
    public List<TeacherDto> getAllTeachers() {
        return teacherRepo.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    // delete teacher
    public String quitTeacher(Long teacherId) {
        TeacherEntity foundTeacher = teacherRepo.findById(teacherId)
                .orElseThrow(() -> new NotFoundExp("Teacher not found"));

        foundTeacher.setDeleted_at(LocalDate.now());
        teacherRepo.save(foundTeacher);

        return "Successfully deleted";
    }

    // Teacher go to leave, not deleted
    public String onLeave(Long teacherId) {
        TeacherEntity foundTeacher = teacherRepo.findById(teacherId)
                .orElseThrow(() -> new NotFoundExp("Teacher not found"));

        foundTeacher.setOn_leave_time(LocalDate.now());
        teacherRepo.save(foundTeacher);

        return "Successfully go to leave";
    }

    private TeacherDto toDto(TeacherEntity entity) {
        TeacherDto dto = new TeacherDto();

        dto.setId(entity.getId());
        dto.setFullName(entity.getFullName());
        dto.setPhoneNum(entity.getPhoneNum());
        dto.setRoles(entity.getRoles());
        dto.setLevelOfKnowledge(entity.getLevelOfKnowledge());
        dto.setCreated_at(entity.getCreated_at());

        return dto;
    }
}
