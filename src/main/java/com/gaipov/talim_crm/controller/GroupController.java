package com.gaipov.talim_crm.controller;

import com.gaipov.talim_crm.dto.GroupDto;
import com.gaipov.talim_crm.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/group")
public class GroupController {
    @Autowired
    GroupService service;

    @PostMapping("/addGroup")
    public ResponseEntity<GroupDto> createGroup(@RequestBody GroupDto dto) {
        return ResponseEntity.ok(service.createGroup(dto));
    }

    @GetMapping("/all")
    public ResponseEntity<List<GroupDto>> all() {
        return ResponseEntity.ok(service.all());
    }
}
