package com.gaipov.talim_crm.service;

import com.gaipov.talim_crm.dto.GroupDto;
import com.gaipov.talim_crm.entity.GroupEntity;
import com.gaipov.talim_crm.entity.StudentEntity;
import com.gaipov.talim_crm.entity.TeacherEntity;
import com.gaipov.talim_crm.exps.NotFoundExp;
import com.gaipov.talim_crm.repository.GroupRepo;
import com.gaipov.talim_crm.repository.TeacherRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class GroupService {
    @Autowired
    GroupRepo groupRepo;

    @Autowired
    TeacherRepo teacherRepo;

    public GroupDto createGroup(GroupDto dto) {
        GroupEntity groupEntity = new GroupEntity();
        groupEntity.setNameOfGroup(dto.getNameOfGroup());
        groupEntity.setTypeOfGroup(dto.getTypeOfGroup());
        groupEntity.setListOfStudents(dto.getListOfStudents());
        groupEntity.setMaxStudents(30);
        groupEntity.setCreated_at(LocalDate.now());

        groupRepo.save(groupEntity);

        return toDto(groupEntity);
    }

    public List<GroupDto> getAllGroups() {
        return groupRepo.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    public Optional<GroupDto> getGroupById(Long id) {
        return groupRepo.findById(id).map(this::toDto);
    }

    public void deleteById(Long id) {
        GroupEntity entity = groupRepo.findById(id).orElseThrow(() -> new NotFoundExp("Group not found"));

        entity.setDeleted_at(LocalDate.now());
        groupRepo.save(entity);
    }

    public String assignTeacherToGroup(Long groupId, Long teacherId) {
        TeacherEntity teacherEntity = teacherRepo.findById(teacherId).orElseThrow(() -> new NotFoundExp("Teacher not found"));

        GroupEntity groupEntity = groupRepo.findById(groupId).orElseThrow(() -> new NotFoundExp("Group not found"));

        groupEntity.setTeacherEntity(teacherEntity);
        groupRepo.save(groupEntity);

        return "Successfully pinned";
    }

    public List<GroupDto> findGroupByName(String groupName) {
        List<GroupEntity> groupEntity = groupRepo.findByNameOfGroup(groupName);

        if (groupEntity.isEmpty()) {
            throw new NotFoundExp("Group not found.");
        }

        return groupEntity.stream()
                .map(this:: toDto)
                .collect(Collectors.toList());
    }

    public Integer getCurrentStudentsCount(Long groupId) {
        GroupEntity entity = groupRepo.findById(groupId)
                .orElseThrow(() -> new NotFoundExp("Group Not Found"));

        return entity.getListOfStudents().size();
    }


    private GroupDto toDto(GroupEntity entity) {
        GroupDto dto = new GroupDto();

        dto.setId(entity.getId());
        dto.setNameOfGroup(entity.getNameOfGroup());
        dto.setTypeOfGroup(entity.getTypeOfGroup());
        dto.setListOfStudents(entity.getListOfStudents());
        dto.setCurrentStudents(dto.getListOfStudents().size());
        dto.setMaxStudents(entity.getMaxStudents());
        dto.setCreated_at(entity.getCreated_at());

        return dto;
    }
}
