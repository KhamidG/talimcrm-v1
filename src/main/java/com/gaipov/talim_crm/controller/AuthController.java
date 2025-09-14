package com.gaipov.talim_crm.controller;

import com.gaipov.talim_crm.dto.StudentDto;
import com.gaipov.talim_crm.dto.TeacherDto;
import com.gaipov.talim_crm.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/auth")
public class AuthController {
    @Autowired
    AuthService service;

    @PostMapping("/createTeacher")
    public ResponseEntity<TeacherDto> createStudent(@RequestBody TeacherDto dto) {
        return ResponseEntity.ok(service.createTeacher(dto));
    }
}
