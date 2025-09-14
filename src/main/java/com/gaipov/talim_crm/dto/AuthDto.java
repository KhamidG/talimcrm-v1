package com.gaipov.talim_crm.dto;

import com.gaipov.talim_crm.enums.UserRole;
import lombok.Data;

import java.time.LocalDate;

@Data
public class AuthDto {
    private Long id;
    private String fullName;
    private String phoneNum;
    private UserRole roles;
    private LocalDate created_at;
}
