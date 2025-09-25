package com.gaipov.talim_crm.entity;

import com.gaipov.talim_crm.enums.UserRole;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Data
@Entity
@Table(name = "auth")
public class AuthEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "phone_num")
    private String phoneNum;

    @Enumerated(EnumType.STRING)
    private UserRole roles;

    @Column(name = "created_at")
    private Date created_at;
}