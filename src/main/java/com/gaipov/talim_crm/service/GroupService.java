package com.gaipov.talim_crm.service;

import com.gaipov.talim_crm.dto.GroupDto;
import com.gaipov.talim_crm.entity.GroupEntity;
import com.gaipov.talim_crm.repository.GroupRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GroupService {
    @Autowired
    GroupRepo groupRepo;

    public GroupDto createGroup(GroupDto dto) {
        GroupEntity groupEntity = new GroupEntity();
        groupEntity.setNameOfGroup(dto.getNameOfGroup());
        groupEntity.setTypeOfGroup(dto.getTypeOfGroup());
        groupEntity.setListOfStudents(dto.getListOfStudents());
        dto.setMaxStudents(30);
        dto.setCreated_at(LocalDate.now());

        groupRepo.save(groupEntity);
        dto.setId(groupEntity.getId());

        return dto;
    }

    public List<GroupDto> all() {
        return groupRepo.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
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
