package com.gaipov.talim_crm.controller;

import com.gaipov.talim_crm.dto.AttendanceDto;
import com.gaipov.talim_crm.dto.GradeDto;
import com.gaipov.talim_crm.dto.GroupDto;
import com.gaipov.talim_crm.dto.StudentDto;
import com.gaipov.talim_crm.dto.TeacherDto;
import com.gaipov.talim_crm.entity.AttendanceEntity;
import com.gaipov.talim_crm.repository.AttendanceRepository;
import com.gaipov.talim_crm.service.Impl.TeacherServiceImpl;
import com.gaipov.talim_crm.service.TeacherService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Controller
@RequestMapping("/v1/teacher")
public class TeacherController {
    private final TeacherService teacherService;
    private final AttendanceRepository attendanceRepository;

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

    @GetMapping("/login")
    public String showLoginPage() {
        return "teacher_login";
    }

    @PostMapping("/login")
    @ResponseBody
    public ResponseEntity<TeacherDto> login(@RequestBody LoginRequest request, jakarta.servlet.http.HttpSession session) {
        try {
            TeacherDto teacher = teacherService.authenticate(request.getUsername(), request.getPassword());
            // mark teacher session
            session.setAttribute("teacherLoggedIn", true);
            session.setAttribute("teacherId", teacher.getId());
            session.setAttribute("teacherUsername", teacher.getUsername());
            return ResponseEntity.ok(teacher);
        } catch (Exception e) {
            return ResponseEntity.status(401).build();
        }
    }

    @GetMapping("/dashboard")
    public String showDashboard() {
        return "teacher_dashboard";
    }

    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<TeacherDto> getTeacherById(@PathVariable("id") Long id) {
        try {
            TeacherDto teacher = teacherService.getTeacherById(id);
            return ResponseEntity.ok(teacher);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/students")
    @ResponseBody
    public ResponseEntity<List<StudentDto>> getTeacherStudents(@RequestHeader("X-Teacher-Id") Long teacherId) {
        try {
            List<StudentDto> students = teacherService.getTeacherStudents(teacherId);
            return ResponseEntity.ok(students);
        } catch (Exception e) {
            return ResponseEntity.ok(List.of());
        }
    }

    @GetMapping("/groups")
    @ResponseBody
    public ResponseEntity<List<GroupDto>> getTeacherGroups(@RequestHeader("X-Teacher-Id") Long teacherId) {
        try {
            List<GroupDto> groups = teacherService.getTeacherGroups(teacherId);
            return ResponseEntity.ok(groups);
        } catch (Exception e) {
            return ResponseEntity.ok(List.of());
        }
    }

    @GetMapping("/group/{groupId}/studentsList")
    @ResponseBody
    public ResponseEntity<List<StudentDto>> getGroupStudentsForTeacher(@PathVariable("groupId") Long groupId,
                                                                       @RequestHeader("X-Teacher-Id") Long teacherId) {
        try {
            return ResponseEntity.ok(teacherService.getGroupStudentsForTeacher(groupId, teacherId));
        } catch (Exception e) {
            return ResponseEntity.ok(List.of());
        }
    }

    @GetMapping("/group/{groupId}/students")
    @ResponseBody
    public ResponseEntity<List<StudentDto>> getGroupStudents(@PathVariable("groupId") Long groupId, 
                                                           @RequestHeader("X-Teacher-Id") Long teacherId) {
        try {
            System.out.println("Getting students for group: " + groupId + ", teacher: " + teacherId);
            List<StudentDto> students = teacherService.getGroupStudents(groupId, teacherId);
            System.out.println("Found " + students.size() + " students");
            return ResponseEntity.ok(students);
        } catch (Exception e) {
            System.err.println("Error getting group students: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().body(List.of());
        }
    }

    @PostMapping("/attendance")
    @ResponseBody
    public ResponseEntity<String> saveAttendance(@RequestBody List<AttendanceDto> attendanceData,
                                                @RequestHeader("X-Teacher-Id") Long teacherId) {
        try {
            teacherService.saveAttendance(attendanceData, teacherId);
            return ResponseEntity.ok("Attendance saved successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to save attendance: " + e.getMessage());
        }
    }

    @GetMapping("/student/{studentId}/attendance")
    @ResponseBody
    public ResponseEntity<List<AttendanceDto>> getStudentAttendance(@PathVariable("studentId") Long studentId,
                                                                     @RequestHeader("X-Teacher-Id") Long teacherId) {
        try {
            List<AttendanceEntity> entities = teacherService.getStudentAttendance(studentId, teacherId);
            List<AttendanceDto> result = entities.stream().map(e -> {
                AttendanceDto dto = new AttendanceDto();
                dto.setId(e.getId());
                if (e.getStudent() != null) {
                    dto.setStudentId(e.getStudent().getId());
                    dto.setStudentName(e.getStudent().getFullName());
                }
                if (e.getGroup() != null) {
                    dto.setGroupId(e.getGroup().getId());
                    dto.setGroupName(e.getGroup().getNameOfGroup());
                }
                dto.setAttendanceDate(e.getAttendanceDate());
                dto.setIsPresent(e.getIsPresent());
                dto.setNotes(e.getNotes());
                dto.setCreatedAt(e.getCreatedAt());
                dto.setCreatedByTeacherId(e.getCreatedByTeacherId());
                return dto;
            }).toList();
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.ok(List.of());
        }
    }

    @GetMapping("/group/{groupId}/page")
    public String showGroupPage(@PathVariable("groupId") Long groupId, Model model) {
        model.addAttribute("groupId", groupId);
        return "teacher_group";
    }

    @GetMapping("/student/{studentId}/attendance/page")
    public String showStudentAttendancePage(@PathVariable("studentId") Long studentId, Model model) {
        model.addAttribute("studentId", studentId);
        return "student_attendance";
    }

    @GetMapping("/student/{studentId}/attendance/history")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getStudentAttendanceHistory(@PathVariable("studentId") Long studentId,
                                                                         @RequestHeader("X-Teacher-Id") Long teacherId) {
        try {
            List<AttendanceDto> attendanceHistory = teacherService.getStudentAttendanceHistory(studentId);
            long missedCount = attendanceRepository.countByStudentIdAndIsPresentFalse(studentId);
            
            Map<String, Object> response = new HashMap<>();
            response.put("attendanceHistory", attendanceHistory);
            response.put("missedCount", missedCount);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.ok(Map.of("attendanceHistory", List.of(), "missedCount", 0L));
        }
    }

    @GetMapping("/statistics")
    public String showStatisticsPage() {
        return "teacher_statistics";
    }

    @GetMapping("/statistics/weekly")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getWeeklyStatistics(@RequestHeader("X-Teacher-Id") Long teacherId) {
        try {
            Map<String, Object> stats = teacherService.getWeeklyStatistics(teacherId);
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.ok(Map.of());
        }
    }

    @GetMapping("/statistics/monthly")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getMonthlyStatistics(@RequestHeader("X-Teacher-Id") Long teacherId) {
        try {
            Map<String, Object> stats = teacherService.getMonthlyStatistics(teacherId);
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.ok(Map.of());
        }
    }

    @PostMapping("/grades")
    @ResponseBody
    public ResponseEntity<String> saveGrades(@RequestBody List<GradeDto> gradeData,
                                            @RequestHeader("X-Teacher-Id") Long teacherId) {
        try {
            teacherService.saveGrades(gradeData, teacherId);
            return ResponseEntity.ok("Grades saved successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to save grades: " + e.getMessage());
        }
    }

    @GetMapping("/student/{studentId}/grades")
    @ResponseBody
    public ResponseEntity<List<GradeDto>> getStudentGrades(@PathVariable("studentId") Long studentId,
                                                           @RequestHeader("X-Teacher-Id") Long teacherId) {
        try {
            List<GradeDto> grades = teacherService.getStudentGrades(studentId, teacherId);
            return ResponseEntity.ok(grades);
        } catch (Exception e) {
            return ResponseEntity.ok(List.of());
        }
    }

    @GetMapping("/group/{groupId}/grades")
    @ResponseBody
    public ResponseEntity<List<GradeDto>> getGroupGrades(@PathVariable("groupId") Long groupId,
                                                         @RequestHeader("X-Teacher-Id") Long teacherId) {
        try {
            List<GradeDto> grades = teacherService.getGroupGrades(groupId, teacherId);
            return ResponseEntity.ok(grades);
        } catch (Exception e) {
            return ResponseEntity.ok(List.of());
        }
    }

    // Inner class for login request
    public static class LoginRequest {
        private String username;
        private String password;

        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }

}
