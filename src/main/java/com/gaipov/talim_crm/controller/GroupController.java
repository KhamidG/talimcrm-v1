package com.gaipov.talim_crm.controller;

import com.gaipov.talim_crm.dto.GroupDto;
import com.gaipov.talim_crm.entity.GroupEntity;
import com.gaipov.talim_crm.enums.GroupRole;
import com.gaipov.talim_crm.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/v1/group")
public class GroupController {
    @Autowired
    private GroupService service;

    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("group", new GroupDto());
        return "group_register";
    }

    @PostMapping("/register")
    public ResponseEntity<GroupDto> createGroup(@RequestBody GroupDto dto) {
        return ResponseEntity.ok(service.createGroup(dto));
    }

    @GetMapping("/listPage")
    public String showGroupsPage(Model model) {
        List<GroupDto> groupDtos = service.getAllGroups();
        model.addAttribute("groups", groupDtos);
        model.addAttribute("groupRoles", GroupRole.values());
        return "groups";
    }

    @GetMapping("/list")
    @ResponseBody
    public ResponseEntity<List<GroupDto>> all() {
        return ResponseEntity.ok(service.getAllGroups());
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteGroup(@PathVariable("id") Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/assignTeacher/{groupId}/{teacherId}")
    public ResponseEntity<String> assignTeacher(@PathVariable Long groupId, @PathVariable Long teacherId) {
        try {
            return ResponseEntity.ok(service.assignTeacherToGroup(groupId, teacherId));
        } catch (Exception e) {
            return ResponseEntity.status(400).body("Error assigning teacher: " + e.getMessage());
        }
    }

    @GetMapping("/byName/{name}")
    public ResponseEntity<List<GroupDto>> findByName(@PathVariable("name") String name) {
        return ResponseEntity.ok(service.findGroupByName(name));
    }

    @GetMapping("/countOfStudents/{id}")
    public ResponseEntity<Integer> getCountOfStudents(@PathVariable("id") Long id) {
        return ResponseEntity.ok(service.getCurrentStudentsCount(id));
    }

    @PutMapping("/edit")
    public ResponseEntity<String> editGroup(@RequestBody GroupEntity group) {
        try {
            service.updateGroup(group);
            return ResponseEntity.ok("Group updated successfully");
        } catch (Exception e) {
            return ResponseEntity.status(400).body("Error updating group: " + e.getMessage());
        }
    }
}