package com.gaipov.talim_crm.repository;

import com.gaipov.talim_crm.entity.StudentEntity;
import com.gaipov.talim_crm.enums.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepo extends JpaRepository<StudentEntity, Long> {
    List<StudentEntity> findByFullName(String name);

    List<StudentEntity> findByStatus(UserStatus userStatus);


}
