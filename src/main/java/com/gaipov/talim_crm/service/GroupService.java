package com.gaipov.talim_crm.service;

import com.gaipov.talim_crm.dto.GroupDto;
import com.gaipov.talim_crm.entity.GroupEntity;

import java.util.List;
import java.util.Optional;

public interface GroupService {
    GroupDto createGroup(GroupDto dto);

    List<GroupDto> getAllGroups();

    Integer getCurrentStudentsCount(Long groupId);

    List<GroupDto> findGroupByName(String groupName);

    void updateGroup(GroupEntity group);

    String assignTeacherToGroup(Long groupId, Long teacherId);

    void deleteById(Long id);

    Optional<GroupDto> getGroupById(Long id);
}
