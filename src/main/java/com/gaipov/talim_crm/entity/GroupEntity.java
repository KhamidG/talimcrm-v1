package com.gaipov.talim_crm.entity;

import com.gaipov.talim_crm.enums.GroupRole;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Entity
@Data
public class GroupEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String nameOfGroup;

//    @ManyToOne
//    private TeacherEntity teacherEntity;

    @Column
    @Enumerated(EnumType.STRING)
    private GroupRole typeOfGroup;

    @Column
    @ManyToMany
    private List<StudentEntity> listOfStudents;

    @Column
    private Integer maxStudents;

    @Column
    private Integer currentStudents;

    @Column
    private LocalDate created_at;
}
