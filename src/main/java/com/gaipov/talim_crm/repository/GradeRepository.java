package com.gaipov.talim_crm.repository;

import com.gaipov.talim_crm.entity.GradeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface GradeRepository extends JpaRepository<GradeEntity, Long> {
    List<GradeEntity> findByStudentIdOrderByLessonDateDesc(Long studentId);
    
    List<GradeEntity> findByGroupIdOrderByLessonDateDesc(Long groupId);

    
    Optional<GradeEntity> findByStudentIdAndGroupIdAndLessonDate(Long studentId, Long groupId, LocalDate lessonDate);

}
