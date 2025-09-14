package com.gaipov.talim_crm.service;

import com.gaipov.talim_crm.dto.TeacherDto;
import com.gaipov.talim_crm.entity.TeacherEntity;
import com.gaipov.talim_crm.exps.NotFoundExp;
import com.gaipov.talim_crm.repository.TeacherRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TeacherService {
    private final TeacherRepo teacherRepo;

    public TeacherDto findTeacherByName(TeacherDto teacherDto) {
        TeacherEntity optionalTeacher = teacherRepo.findByFullName(teacherDto.getFullName())
                .orElseThrow(() -> new NotFoundExp("Teacher is not found."));

        teacherDto.setFullName(optionalTeacher.getFullName());
        teacherDto.setPhoneNum(optionalTeacher.getPhoneNum());
        teacherDto.setRoles(optionalTeacher.getRoles());
        teacherDto.setLevelOfKnowledge(optionalTeacher.getLevelOfKnowledge());
        teacherDto.setCreated_at(optionalTeacher.getCreated_at());

        return teacherDto;
    }
}
