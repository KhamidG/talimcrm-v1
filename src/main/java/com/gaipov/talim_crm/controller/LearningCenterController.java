package com.gaipov.talim_crm.controller;

import com.gaipov.talim_crm.dto.LearningCenterDto;
import com.gaipov.talim_crm.service.LearningCenterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/v1/center")
@RequiredArgsConstructor
public class LearningCenterController {
    private final LearningCenterService learningCenterService;

    @GetMapping("/login")
    public String showLoginPage() {
        return "center_login";
    }

    @PostMapping("/login")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> credentials) {
        try {
            String username = credentials.get("username");
            String password = credentials.get("password");
            
            // Simple hardcoded credentials for training center
            if ("admin".equals(username) && "admin123".equals(password)) {
                LearningCenterDto center = new LearningCenterDto();
                center.setId(1L);
                center.setCenterName("Training Center");
                center.setUsername("admin");
                center.setAddress("Main Office");
                center.setPhone("+1234567890");
                center.setEmail("admin@trainingcenter.com");
                center.setIsActive(true);
                
                return ResponseEntity.ok(Map.of(
                    "success", true,
                    "center", center,
                    "message", "Login successful"
                ));
            } else {
                return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "Invalid credentials"
                ));
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", e.getMessage()
            ));
        }
    }

    @GetMapping("/dashboard")
    public String showDashboard(@RequestParam Long centerId, Model model) {
        // Simple hardcoded center data
        LearningCenterDto center = new LearningCenterDto();
        center.setId(1L);
        center.setCenterName("Training Center");
        center.setUsername("admin");
        center.setAddress("Main Office");
        center.setPhone("+1234567890");
        center.setEmail("admin@trainingcenter.com");
        center.setIsActive(true);
        
        model.addAttribute("center", center);
        return "center_dashboard";
    }

    @GetMapping("/management")
    public String showManagementPage() {
        return "center_management";
    }

    @GetMapping("/register")
    public String showRegisterPage() {
        return "center_register";
    }

    @PostMapping("/register")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> register(@RequestBody LearningCenterDto dto) {
        try {
            LearningCenterDto created = learningCenterService.createCenter(dto);
            return ResponseEntity.ok(Map.of(
                "success", true,
                "center", created,
                "message", "Center registered successfully"
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", e.getMessage()
            ));
        }
    }

    @GetMapping("/all")
    @ResponseBody
    public ResponseEntity<List<LearningCenterDto>> getAllCenters() {
        return ResponseEntity.ok(learningCenterService.getAllCenters());
    }

    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<LearningCenterDto> getCenterById(@PathVariable Long id) {
        Optional<LearningCenterDto> center = learningCenterService.getCenterById(id);
        return center.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> updateCenter(@PathVariable Long id, @RequestBody LearningCenterDto dto) {
        try {
            LearningCenterDto updated = learningCenterService.updateCenter(id, dto);
            return ResponseEntity.ok(Map.of(
                "success", true,
                "center", updated,
                "message", "Center updated successfully"
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", e.getMessage()
            ));
        }
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> deactivateCenter(@PathVariable Long id) {
        try {
            learningCenterService.deactivateCenter(id);
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Center deactivated successfully"
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", e.getMessage()
            ));
        }
    }
}
