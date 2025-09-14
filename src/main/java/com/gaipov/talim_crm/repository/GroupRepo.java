package com.gaipov.talim_crm.repository;

import com.gaipov.talim_crm.entity.GroupEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupRepo extends JpaRepository<GroupEntity, Long> {
}
