package com.gaipov.talim_crm.service.Impl;

import com.gaipov.talim_crm.dto.AuthDto;
import com.gaipov.talim_crm.entity.AuthEntity;
import com.gaipov.talim_crm.enums.UserRole;
import com.gaipov.talim_crm.enums.UserStatus;
import com.gaipov.talim_crm.repository.ProfileRepository;
import com.gaipov.talim_crm.service.AuthService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final ProfileRepository profileRepository;

    @Override
    public AuthDto registerNewUser(AuthDto dto) {
        AuthEntity entity = new AuthEntity();

        entity.setFullName(dto.getFullName());
        entity.setPhoneNum(dto.getPhoneNum());
        entity.setRoles(dto.getRoles() != null ? dto.getRoles() : UserRole.NEW_USER);
        entity.setStatus(UserStatus.IN_REGISTER);
        entity.setCreated_at(new Date());

        profileRepository.save(entity);
        dto.setId(entity.getId());
        dto.setStatus(entity.getStatus());
        dto.setCreated_at(entity.getCreated_at());

        return dto;
    }

    @Override
    public List<AuthDto> listOfUsers() {
        return profileRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }


    private AuthDto toDto(AuthEntity entity) {
        AuthDto dto = new AuthDto();
        dto.setId(entity.getId());
        dto.setFullName(entity.getFullName());
        dto.setPhoneNum(entity.getPhoneNum());
        dto.setRoles(entity.getRoles());
        dto.setStatus(entity.getStatus());
        dto.setCreated_at(entity.getCreated_at());
        return dto;
    }
}
