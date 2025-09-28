package com.gaipov.talim_crm.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class TeacherProfileEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String passwordHash;

    @OneToOne
    @JoinColumn(name = "teacher_id")
    private TeacherEntity teacher;
}
