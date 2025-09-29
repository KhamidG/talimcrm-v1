package com.gaipov.talim_crm.repository;

import com.gaipov.talim_crm.entity.AuthEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfileRepository extends JpaRepository<AuthEntity, Long> {

}
