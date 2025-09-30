package com.gaipov.talim_crm.service;

import com.gaipov.talim_crm.dto.AuthDto;

import java.util.List;

public interface AuthService {
    AuthDto registerNewUser(AuthDto dto);

    List<AuthDto> listOfUsers();
}
