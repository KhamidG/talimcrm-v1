package com.gaipov.talim_crm.controller;

import com.gaipov.talim_crm.dto.StudentDto;
import com.gaipov.talim_crm.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/student")
public class StudentController {
    @Autowired
    StudentService service;

    @PostMapping("/register")
    public ResponseEntity<StudentDto> register(@RequestBody StudentDto dto) {
        return ResponseEntity.ok(service.registerNewStudent(dto));
    }
}
