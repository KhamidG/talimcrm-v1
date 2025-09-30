package com.gaipov.talim_crm.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "grades")
@Data
public class GradeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private StudentEntity student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", nullable = false)
    private GroupEntity group;

    @Column(name = "grade_value", nullable = false)
    private Double gradeValue; // Оценка по шкале IELTS (0.0 - 9.0 с шагом 0.5)

    @Column(name = "lesson_date", nullable = false)
    private LocalDate lessonDate;

    @Column(name = "lesson_topic")
    private String lessonTopic; // Тема урока

    @Column(name = "comment")
    private String comment; // Комментарий учителя

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "created_by_teacher_id")
    private Long createdByTeacherId;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
