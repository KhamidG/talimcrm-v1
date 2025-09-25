package com.gaipov.talim_crm.entity;

import com.gaipov.talim_crm.enums.GroupRole;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "groups")
@Data
public class GroupEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name_of_group", nullable = false)
    private String nameOfGroup;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_id", nullable = true)
    private TeacherEntity teacher;

    @Column(name = "type_of_group", nullable = false)
    @Enumerated(EnumType.STRING)
    private GroupRole typeOfGroup;

    @OneToMany
    @JoinColumn(name = "group_id")
    private List<StudentEntity> listOfStudents;

    @Column(name = "max_students")
    private Integer maxStudents;

    @Column(name = "current_students")
    private Integer currentStudents;

    @Column(name = "created_at")
    private LocalDate created_at;

    @Column(name = "deleted_at")
    private LocalDate deleted_at;
}