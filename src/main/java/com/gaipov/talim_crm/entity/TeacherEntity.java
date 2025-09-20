package com.gaipov.talim_crm.entity;

import com.gaipov.talim_crm.enums.UserRole;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "teachers_entity")
public class TeacherEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String fullName;

    @Column
    private String phoneNum;

    @Column
    @Enumerated(EnumType.STRING)
    private UserRole roles;

    @Column
    private String levelOfKnowledge;

    @Column
    private LocalDate created_at;

    @Column
    private LocalDate deleted_at;

    @Column
    private LocalDate on_leave_time;
}
