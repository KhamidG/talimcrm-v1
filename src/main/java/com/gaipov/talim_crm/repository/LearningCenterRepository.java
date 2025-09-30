package com.gaipov.talim_crm.repository;

import com.gaipov.talim_crm.entity.LearningCenterEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LearningCenterRepository extends JpaRepository<LearningCenterEntity, Long> {

    Optional<LearningCenterEntity> findByUsername(String username);

    Optional<LearningCenterEntity> findByUsernameAndIsActiveTrue(String username);

    boolean existsByUsername(String username);

    boolean existsByCenterName(String centerName);
}
