package com.gaipov.talim_crm.controller;

import com.gaipov.talim_crm.dto.GroupDto;
import com.gaipov.talim_crm.entity.StudentEntity;
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
        return ResponseEntity.ok(service.getAllGroups());
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteGroup(@PathVariable("id") Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/assignTeacher/{gI}/{tI}")
    public ResponseEntity<String> assignTeacherToGroup(@PathVariable("gI") Long groupId,
                                                       @PathVariable("tI") Long teacherId) {
        return ResponseEntity.ok(service.assignTeacherToGroup(groupId, teacherId));
    }

    @GetMapping("/byName/{name}")
    public ResponseEntity<List<GroupDto>> findByName(@PathVariable("name") String name) {
        return ResponseEntity.ok(service.findGroupByName(name));
    }

    @GetMapping("/countOfStudents/{id}")
    public ResponseEntity<Integer> getCountOfStudents(@PathVariable("id") Long id) {
        return ResponseEntity.ok(service.getCurrentStudentsCount(id));
    }
}
