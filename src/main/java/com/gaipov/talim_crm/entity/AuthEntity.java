package com.gaipov.talim_crm.entity;

import com.gaipov.talim_crm.enums.UserRole;
import com.gaipov.talim_crm.enums.UserStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "auth")
public class AuthEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String password;
    @Enumerated(EnumType.STRING)
    private UserRole roles;

    @ManyToOne
    @JoinColumn(name = "center_id")
    private CenterEntity center;
}