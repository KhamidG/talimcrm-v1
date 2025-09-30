package com.gaipov.talim_crm.repository;

import com.gaipov.talim_crm.dto.GroupDto;
import com.gaipov.talim_crm.entity.GroupEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupRepo extends JpaRepository<GroupEntity, Long> {
    @Query("""
            select new com.gaipov.talim_crm.dto.GroupDto(
            id,
            
            )
            from GroupEntity
            """)
    List<GroupDto> findByNameOfGroup(String name);
    List<GroupEntity> findByTeacherId(Long teacherId);
}
