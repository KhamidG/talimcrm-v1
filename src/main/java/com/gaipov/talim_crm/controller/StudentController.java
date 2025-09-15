package com.gaipov.talim_crm.controller;

import com.gaipov.talim_crm.dto.StudentDto;
import com.gaipov.talim_crm.enums.UserRole;
import com.gaipov.talim_crm.enums.UserStatus;
import com.gaipov.talim_crm.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/v1/student")
public class StudentController {
    @Autowired
    StudentService service;

    @PostMapping("/register")
    public ResponseEntity<StudentDto> register(@RequestBody StudentDto dto) {
        return ResponseEntity.ok(service.registerNewStudent(dto));
    }

    @GetMapping("/list")
    public ResponseEntity<List<StudentDto>> getAllStudentsList() {
        return ResponseEntity.ok(service.getAllStudents());
    }

    @GetMapping("/byId/{id}")
    public ResponseEntity<Optional<StudentDto>> getById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(service.getStudentById(id));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable("id") Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/findByName/{name}")
    public ResponseEntity<List<StudentDto>> findByName(@PathVariable("name") String name) {
        return ResponseEntity.ok(service.findByFullName(name));
    }

    @GetMapping("/sortByStatus/{status}")
    public ResponseEntity<List<StudentDto>> findByName(@PathVariable("status") UserStatus userStatus) {
        return ResponseEntity.ok(service.sortByStatus(userStatus));
    }

    @PostMapping("/addToGroup/{studentId}/{groupId}")
    public ResponseEntity<String> addToGroup(@PathVariable("studentId") Long studentId,
                                             @PathVariable("groupId") Long groupId) {
        return ResponseEntity.ok(service.assignStudentToGroup(studentId, groupId));
    }

    @DeleteMapping("/remove/{studentId}/{groupId}")
    public ResponseEntity<String> removeFromGroup(@PathVariable("studentId") Long studentId,
                                                  @PathVariable("groupId") Long groupId) {
        return ResponseEntity.ok(service.removeStudentFromGroup(studentId, groupId));
    }
}
