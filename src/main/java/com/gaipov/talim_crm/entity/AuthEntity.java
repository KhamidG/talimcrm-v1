package com.gaipov.talim_crm.entity;

import com.gaipov.talim_crm.enums.UserRole;
import com.gaipov.talim_crm.enums.UserStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Data
@Entity
@Table(name = "auth")
public class AuthEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "full_name")
    private String fullName;

    @Column
    private String username;

    @Column
    private String password;

    @Column(name = "phone_num")
    private String phoneNum;

    @Enumerated(EnumType.STRING)
    private UserRole roles;

    @Enumerated(EnumType.STRING)
    private UserStatus status;

    @Column(name = "created_at")
    private Date created_at;


}