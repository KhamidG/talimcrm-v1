package com.gaipov.talim_crm.repository;

import com.gaipov.talim_crm.entity.TeacherEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TeacherRepo extends JpaRepository<TeacherEntity, Long> {
    Optional<TeacherEntity> findByFullName(String fullName);
}
