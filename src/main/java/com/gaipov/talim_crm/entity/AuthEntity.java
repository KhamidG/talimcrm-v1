package com.gaipov.talim_crm.entity;

import com.gaipov.talim_crm.enums.UserRole;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@Table(name = "auth_entity")
public class AuthEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String fullName;

    @Column
    private String phoneNum;

    @Column
    @Enumerated(EnumType.STRING)
    private UserRole roles = UserRole.NEW_USER;

    @Column
    private LocalDate created_at;
}
