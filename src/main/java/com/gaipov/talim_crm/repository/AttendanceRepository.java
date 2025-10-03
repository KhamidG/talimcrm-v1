package com.gaipov.talim_crm.repository;

import com.gaipov.talim_crm.entity.AttendanceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AttendanceRepository extends JpaRepository<AttendanceEntity, Long> {

    Optional<AttendanceEntity> findByStudentIdAndGroupIdAndAttendanceDate(Long studentId, Long groupId, LocalDate date);

    List<AttendanceEntity> findByStudentIdOrderByAttendanceDateDesc(Long studentId);

    long countByStudentIdAndIsPresentFalse(Long studentId);
}
