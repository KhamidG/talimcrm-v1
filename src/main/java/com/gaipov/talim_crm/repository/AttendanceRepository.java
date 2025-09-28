package com.gaipov.talim_crm.repository;

import com.gaipov.talim_crm.entity.AttendanceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AttendanceRepository extends JpaRepository<AttendanceEntity, Long> {
    
    List<AttendanceEntity> findByGroupIdAndAttendanceDate(Long groupId, LocalDate date);
    
    List<AttendanceEntity> findByStudentIdAndGroupId(Long studentId, Long groupId);
    
    Optional<AttendanceEntity> findByStudentIdAndGroupIdAndAttendanceDate(Long studentId, Long groupId, LocalDate date);
    
    @Query("SELECT a FROM AttendanceEntity a WHERE a.group.id IN " +
           "(SELECT g.id FROM GroupEntity g WHERE g.teacher.id = :teacherId)")
    List<AttendanceEntity> findByTeacherId(@Param("teacherId") Long teacherId);
    
    @Query("SELECT a FROM AttendanceEntity a WHERE a.group.id = :groupId AND a.attendanceDate BETWEEN :startDate AND :endDate")
    List<AttendanceEntity> findByGroupIdAndDateRange(@Param("groupId") Long groupId, 
                                                    @Param("startDate") LocalDate startDate, 
                                                    @Param("endDate") LocalDate endDate);

    List<AttendanceEntity> findByStudentIdOrderByAttendanceDateDesc(Long studentId);

    List<AttendanceEntity> findByStudentIdAndIsPresentFalseOrderByAttendanceDateDesc(Long studentId);
    
    List<AttendanceEntity> findByStudentId(Long studentId);
    
    long countByStudentIdAndIsPresentFalse(Long studentId);
}
