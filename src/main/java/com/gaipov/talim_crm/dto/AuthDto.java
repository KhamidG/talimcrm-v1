package com.gaipov.talim_crm.dto;

import com.gaipov.talim_crm.enums.UserRole;
import lombok.Data;

import java.time.LocalDate;
import java.util.Date;

@Data
public class AuthDto {
    private Long id;
    private String fullName;
    private String phoneNum;
    private UserRole roles;
    private Date created_at;
}
