package com.gaipov.talim_crm.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class LearningCenterDto {
    private Long id;
    private String centerName;
    private String username;
    private String password;
    private String address;
    private String phone;
    private String email;
    private LocalDate created_at;
    private LocalDate deleted_at;
    private Boolean isActive;
}
