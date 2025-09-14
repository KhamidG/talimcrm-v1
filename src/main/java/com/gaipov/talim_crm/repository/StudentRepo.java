package com.gaipov.talim_crm.repository;

import com.gaipov.talim_crm.entity.StudentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentRepo extends JpaRepository<StudentEntity, Long> {
    Optional<StudentEntity> findByFullName(String name);
}
