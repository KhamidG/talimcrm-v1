package com.gaipov.talim_crm.controller;

import com.gaipov.talim_crm.dto.GroupDto;
import com.gaipov.talim_crm.dto.TeacherDto;
import com.gaipov.talim_crm.enums.GroupRole;
import com.gaipov.talim_crm.service.TeacherService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/v1/teacher")
public class TeacherController {
    private final TeacherService teacherService;

    @GetMapping("/register")
    public String registerForm(Model model){
        model.addAttribute("teacher", new TeacherDto());
        return "teacher_register";
    }

    @PostMapping("/register")
    public ResponseEntity<TeacherDto> createTeacher(@RequestBody TeacherDto dto) {
        return ResponseEntity.ok(teacherService.createTeacher(dto));
    }

    @GetMapping("/listPage")
    public String showGroupsPage(Model model) {
        List<TeacherDto> teacherDtos = teacherService.getAllTeachers();
        model.addAttribute("teachers", teacherDtos);
        return "teachers";
    }


    @GetMapping("/list")
    @ResponseBody
    public ResponseEntity<List<TeacherDto>> listOfAllTeachers() {
        return ResponseEntity.ok(teacherService.listOfDto());
    }

    @GetMapping("/findByName/{name}")
    public ResponseEntity<List<TeacherDto>> findByName(@PathVariable("name") String name) {
        return ResponseEntity.ok(teacherService.findByNameOfTeacher(name));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteTeacher(@PathVariable("id") Long id) {
        teacherService.quitTeacher(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/onLeave/{id}")
    public ResponseEntity<Void> onLeave(@PathVariable("id") Long id) {
        teacherService.onLeave(id);
        return ResponseEntity.noContent().build();
    }


}
