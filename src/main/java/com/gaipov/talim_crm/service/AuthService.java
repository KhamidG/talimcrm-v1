package com.gaipov.talim_crm.service;

import com.gaipov.talim_crm.dto.AuthDto;
import com.gaipov.talim_crm.dto.PaymentDto;
import com.gaipov.talim_crm.entity.AuthEntity;
import com.gaipov.talim_crm.entity.PaymentEntity;
import com.gaipov.talim_crm.enums.UserRole;
import com.gaipov.talim_crm.repository.ProfileRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class AuthService {
    private final ProfileRepository profileRepository;

    public AuthDto registerNewUser(AuthDto dto) {
        AuthEntity entity = new AuthEntity();

        entity.setFullName(dto.getFullName());
        entity.setPhoneNum(dto.getPhoneNum());
        entity.setRoles(UserRole.NEW_USER);

        profileRepository.save(entity);
        dto.setId(entity.getId());

        return dto;
    }

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
        dto.setCreated_at(entity.getCreated_at()); // Fix: Use entity's created_at
        return dto;
    }
}
