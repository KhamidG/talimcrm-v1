package com.gaipov.talim_crm.dto;

import com.gaipov.talim_crm.enums.UserRole;
import com.gaipov.talim_crm.enums.UserStatus;
import lombok.Data;

import java.time.LocalDate;
import java.util.Date;

@Data
public class AuthDto {
    private Long id;
    private String fullName;
    private String phoneNum;
    private String username;
    private String password;
    private UserRole roles;
    private UserStatus status;
    private Date created_at;
}
