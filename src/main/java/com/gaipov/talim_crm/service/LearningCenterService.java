package com.gaipov.talim_crm.service;

import com.gaipov.talim_crm.dto.LearningCenterDto;
import com.gaipov.talim_crm.entity.LearningCenterEntity;
import com.gaipov.talim_crm.exps.NotFoundExp;
import com.gaipov.talim_crm.repository.LearningCenterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LearningCenterService {
    private final LearningCenterRepository learningCenterRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public LearningCenterDto createCenter(LearningCenterDto dto) {
        if (learningCenterRepository.existsByUsername(dto.getUsername())) {
            throw new RuntimeException("Username already exists");
        }
        if (learningCenterRepository.existsByCenterName(dto.getCenterName())) {
            throw new RuntimeException("Center name already exists");
        }

        LearningCenterEntity entity = new LearningCenterEntity();
        entity.setCenterName(dto.getCenterName());
        entity.setUsername(dto.getUsername());
        entity.setPassword(passwordEncoder.encode(dto.getPassword()));
        entity.setAddress(dto.getAddress());
        entity.setPhone(dto.getPhone());
        entity.setEmail(dto.getEmail());
        entity.setCreated_at(LocalDate.now());
        entity.setIsActive(true);

        LearningCenterEntity saved = learningCenterRepository.save(entity);
        return toDto(saved);
    }

    public LearningCenterDto authenticate(String username, String password) {
        Optional<LearningCenterEntity> centerOpt = learningCenterRepository.findByUsernameAndIsActiveTrue(username);
        if (centerOpt.isEmpty()) {
            throw new NotFoundExp("Center not found or inactive");
        }

        LearningCenterEntity center = centerOpt.get();
        if (!passwordEncoder.matches(password, center.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        return toDto(center);
    }

    public Optional<LearningCenterDto> getCenterById(Long id) {
        return learningCenterRepository.findById(id)
                .map(this::toDto);
    }

    public List<LearningCenterDto> getAllCenters() {
        return learningCenterRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public LearningCenterDto updateCenter(Long id, LearningCenterDto dto) {
        LearningCenterEntity entity = learningCenterRepository.findById(id)
                .orElseThrow(() -> new NotFoundExp("Center not found"));

        if (dto.getCenterName() != null) {
            entity.setCenterName(dto.getCenterName());
        }
        if (dto.getAddress() != null) {
            entity.setAddress(dto.getAddress());
        }
        if (dto.getPhone() != null) {
            entity.setPhone(dto.getPhone());
        }
        if (dto.getEmail() != null) {
            entity.setEmail(dto.getEmail());
        }
        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            entity.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

        LearningCenterEntity saved = learningCenterRepository.save(entity);
        return toDto(saved);
    }

    public void deactivateCenter(Long id) {
        LearningCenterEntity entity = learningCenterRepository.findById(id)
                .orElseThrow(() -> new NotFoundExp("Center not found"));
        entity.setIsActive(false);
        entity.setDeleted_at(LocalDate.now());
        learningCenterRepository.save(entity);
    }

    private LearningCenterDto toDto(LearningCenterEntity entity) {
        LearningCenterDto dto = new LearningCenterDto();
        dto.setId(entity.getId());
        dto.setCenterName(entity.getCenterName());
        dto.setUsername(entity.getUsername());
        dto.setAddress(entity.getAddress());
        dto.setPhone(entity.getPhone());
        dto.setEmail(entity.getEmail());
        dto.setCreated_at(entity.getCreated_at());
        dto.setDeleted_at(entity.getDeleted_at());
        dto.setIsActive(entity.getIsActive());
        return dto;
    }
}
