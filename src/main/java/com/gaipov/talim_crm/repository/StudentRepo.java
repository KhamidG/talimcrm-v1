package com.gaipov.talim_crm.repository;

import com.gaipov.talim_crm.entity.StudentEntity;
import com.gaipov.talim_crm.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepo extends JpaRepository<StudentEntity, Long> {
    Optional<StudentEntity> findByFullName(String name);
}
