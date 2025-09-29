package com.gaipov.talim_crm.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
@Table(name = "learning_centers")
public class LearningCenterEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String centerName;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column
    private String address;

    @Column
    private String phone;

    @Column
    private String email;

    @Column
    private LocalDate created_at;

    @Column
    private LocalDate deleted_at;

    @Column
    private Boolean isActive = true;
}
