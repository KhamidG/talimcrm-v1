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
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> credentials, jakarta.servlet.http.HttpSession session) {
        try {
            String username = credentials.get("username");
            String password = credentials.get("password");

            LearningCenterDto center = learningCenterService.authenticate(username, password);

            // Mark center session (separate from staff session)
            session.setAttribute("centerLoggedIn", true);
            session.setAttribute("centerId", center.getId());
            session.setAttribute("centerName", center.getCenterName());
            session.setAttribute("centerUsername", center.getUsername());

            return ResponseEntity.ok(Map.of(
                "success", true,
                "center", center,
                "message", "Login successful"
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", e.getMessage()
            ));
        }
    }

    @GetMapping("/dashboard")
    public String showDashboard(@RequestParam(required = false) Long centerId, Model model, jakarta.servlet.http.HttpSession session) {
        Boolean centerLoggedIn = (Boolean) session.getAttribute("centerLoggedIn");
        Long sessionCenterId = (Long) session.getAttribute("centerId");

        if (centerLoggedIn == null || !centerLoggedIn || sessionCenterId == null) {
            return "redirect:/v1/center/login";
        }

        Long idToLoad = (centerId != null) ? centerId : sessionCenterId;
        Optional<LearningCenterDto> centerOpt = learningCenterService.getCenterById(idToLoad);
        if (centerOpt.isEmpty()) {
            return "redirect:/v1/center/login";
        }

        model.addAttribute("center", centerOpt.get());
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
