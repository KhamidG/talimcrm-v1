package com.gaipov.talim_crm.controller;

import com.gaipov.talim_crm.dto.StudentDto;
import com.gaipov.talim_crm.enums.UserStatus;
import com.gaipov.talim_crm.service.Impl.StudentServiceImpl;
import com.gaipov.talim_crm.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/v1/student")
public class StudentController {
    @Autowired
    StudentService service;

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("student", new StudentDto());
        return "register";
    }

    @PostMapping("/register")
    @ResponseBody
    public ResponseEntity<StudentDto> register(@RequestBody StudentDto dto) {
        return ResponseEntity.ok(service.registerNewStudent(dto));
    }

    @GetMapping("/listPage")
    public String showStudentsPage(Model model) {
        List<StudentDto> students = service.getAllStudents();
        model.addAttribute("students", students);
        model.addAttribute("userStatuses", UserStatus.values());
        return "students";
    }

    @GetMapping("/list")
    @ResponseBody
    public ResponseEntity<List<StudentDto>> getAllStudentsList() {
        return ResponseEntity.ok(service.getAllStudents());
    }

    @GetMapping("/byId/{id}")
    @ResponseBody
    public ResponseEntity<Optional<StudentDto>> getById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(service.getStudentById(id));
    }

    @DeleteMapping("/delete/{id}")
    @ResponseBody
    public ResponseEntity<Void> deleteById(@PathVariable("id") Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/findByName/{name}")
    @ResponseBody
    public ResponseEntity<List<StudentDto>> findByName(@PathVariable("name") String name) {
        return ResponseEntity.ok(service.findByFullName(name));
    }

    @GetMapping("/sortByStatus/{status}")
    @ResponseBody
    public ResponseEntity<List<StudentDto>> findByName(@PathVariable("status") UserStatus userStatus) {
        return ResponseEntity.ok(service.sortByStatus(userStatus));
    }

    @PostMapping("/addToGroup/{studentId}/{groupId}")
    @ResponseBody
    public ResponseEntity<String> addToGroup(@PathVariable("studentId") Long studentId,
                                             @PathVariable("groupId") Long groupId) {
        return ResponseEntity.ok(service.assignStudentToGroup(studentId, groupId));
    }

    @DeleteMapping("/remove/{studentId}/{groupId}")
    @ResponseBody
    public ResponseEntity<String> removeFromGroup(@PathVariable("studentId") Long studentId,
                                                  @PathVariable("groupId") Long groupId) {
        return ResponseEntity.ok(service.removeStudentFromGroup(studentId, groupId));
    }

    @GetMapping("/details/{id}")
    public String showDetails(@PathVariable("id") Long id, Model model) {
        Optional<StudentDto> studentOptional = service.getStudentById(id);
        if (studentOptional.isPresent()) {
            model.addAttribute("student", studentOptional.get());
            return "details";
        } else {
            return "redirect:/v1/student/listPage";
        }
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        Optional<StudentDto> studentOptional = service.getStudentById(id);
        if (studentOptional.isPresent()) {
            model.addAttribute("student", studentOptional.get());
            model.addAttribute("userStatuses", UserStatus.values());
            return "edit";
        } else {
            return "redirect:/v1/student/listPage";
        }
    }

    @PutMapping("/edit")
    @ResponseBody
    public ResponseEntity<StudentDto> editStudent(@RequestBody StudentDto studentDto) {
        return ResponseEntity.ok(service.updateStudent(studentDto));
    }

    @GetMapping("/count/{status}")
    @ResponseBody
    public ResponseEntity<Integer> studentsCountByStatus(@PathVariable("status") UserStatus status) {
        Integer count = service.studentsCountWithActiveStatus(status);
        return ResponseEntity.ok(count);
    }

    @PostMapping("/convertFromUser")
    @ResponseBody
    public ResponseEntity<StudentDto> convertFromUser(@RequestBody Map<String, Object> request) {
        try {
            Long userId = Long.valueOf(request.get("userId").toString());
            String fullName = request.get("fullName").toString();
            String phoneNum = request.get("phoneNum").toString();
            
            StudentDto createdStudent = service.convertFromUser(userId, fullName, phoneNum);
            return ResponseEntity.ok(createdStudent);
        } catch (Exception e) {
            return ResponseEntity.status(400).body(null);
        }
    }
}

