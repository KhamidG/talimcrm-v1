package com.gaipov.talim_crm.service.Impl;

import com.gaipov.talim_crm.dto.GroupDto;
import com.gaipov.talim_crm.entity.GroupEntity;
import com.gaipov.talim_crm.entity.TeacherEntity;
import com.gaipov.talim_crm.exps.NotFoundExp;
import com.gaipov.talim_crm.repository.GroupRepo;
import com.gaipov.talim_crm.repository.TeacherRepo;
import com.gaipov.talim_crm.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class GroupServiceImpl implements GroupService {

    @Autowired
    private GroupRepo groupRepo;

    @Autowired
    private TeacherRepo teacherRepo;

    @Override
    public GroupDto createGroup(GroupDto dto) {
        GroupEntity groupEntity = new GroupEntity();
        groupEntity.setNameOfGroup(dto.getNameOfGroup());
        groupEntity.setTypeOfGroup(dto.getTypeOfGroup());
        groupEntity.setListOfStudents(dto.getListOfStudents());
        groupEntity.setMaxStudents(dto.getMaxStudents());
        groupEntity.setLessonStartTime(dto.getLessonStartTime());
        groupEntity.setLessonEndTime(dto.getLessonEndTime());
        groupEntity.setLessonDays(dto.getLessonDays());


        // Assign teacher if provided
        if (dto.getTeacherId() != null) {
            TeacherEntity teacher = teacherRepo.findById(dto.getTeacherId())
                    .orElseThrow(() -> new NotFoundExp("Teacher not found"));
            groupEntity.setTeacher(teacher);
        }

        groupRepo.save(groupEntity);

        return toDto(groupEntity);
    }

    @Override
    public List<GroupDto> getAllGroups() {
        return groupRepo.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<GroupDto> getGroupById(Long id) {
        return groupRepo.findById(id).map(this::toDto);
    }

    @Override
    public void deleteById(Long id) {
        GroupEntity entity = groupRepo.findById(id)
                .orElseThrow(() -> new NotFoundExp("Group not found"));

//        entity.setDeleted_at(LocalDate.now());
        groupRepo.save(entity);
    }

    @Transactional
    @Override
    public String assignTeacherToGroup(Long groupId, Long teacherId) {
        TeacherEntity teacherEntity = teacherRepo.findById(teacherId)
                .orElseThrow(() -> new NotFoundExp("Teacher not found with ID: " + teacherId));

        GroupEntity groupEntity = groupRepo.findById(groupId)
                .orElseThrow(() -> new NotFoundExp("Group not found with ID: " + groupId));

        groupEntity.setTeacher(teacherEntity);
        groupRepo.save(groupEntity);

        return "Teacher assigned successfully";
    }

    @Override
    public List<GroupDto> findGroupByName(String groupName) {
        var groupEntities = groupRepo.findByNameOfGroup(groupName);

        if (groupEntities.isEmpty()) {
            throw new NotFoundExp("Group not found.");
        }

        return new ArrayList<>(groupEntities);
    }

    @Override
    public Integer getCurrentStudentsCount(Long groupId) {
        GroupEntity entity = groupRepo.findById(groupId)
                .orElseThrow(() -> new NotFoundExp("Group Not Found"));

        return entity.getListOfStudents() != null ? entity.getListOfStudents().size() : 0;
    }

    private GroupDto toDto(GroupEntity entity) {
        GroupDto dto = new GroupDto();
        dto.setId(entity.getId());
        dto.setNameOfGroup(entity.getNameOfGroup());
        dto.setTeacherFullName(entity.getTeacher() != null ? entity.getTeacher().getFullName() : null);
        dto.setTeacherId(entity.getTeacher() != null ? entity.getTeacher().getId() : null);
        dto.setTypeOfGroup(entity.getTypeOfGroup());
        dto.setListOfStudents(entity.getListOfStudents());
        dto.setCurrentStudents(entity.getListOfStudents() != null ? entity.getListOfStudents().size() : 0);
        dto.setMaxStudents(entity.getMaxStudents());
        dto.setLessonStartTime(entity.getLessonStartTime());
        dto.setLessonEndTime(entity.getLessonEndTime());
        dto.setLessonDays(entity.getLessonDays());
//        dto.setCreated_at(entity.getCreated_at());
        return dto;
    }

    @Override
    public void updateGroup(GroupEntity group) {
        Optional<GroupEntity> existingGroup = groupRepo.findById(group.getId());
        if (existingGroup.isPresent()) {
            GroupEntity updatedGroup = existingGroup.get();
            updatedGroup.setNameOfGroup(group.getNameOfGroup());
            updatedGroup.setTypeOfGroup(group.getTypeOfGroup());
            updatedGroup.setMaxStudents(group.getMaxStudents());
            groupRepo.save(updatedGroup);
        } else {
            throw new NotFoundExp("Group not found with ID: " + group.getId());
        }
    }
}