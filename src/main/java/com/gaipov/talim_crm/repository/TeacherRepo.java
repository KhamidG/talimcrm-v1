package com.gaipov.talim_crm.repository;

import com.gaipov.talim_crm.entity.TeacherEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeacherRepo extends JpaRepository<TeacherEntity, Long> {
    List<TeacherEntity> findByFullName(String fullName);
}
