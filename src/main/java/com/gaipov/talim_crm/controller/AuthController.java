package com.gaipov.talim_crm.controller;

import com.gaipov.talim_crm.dto.AuthDto;
import com.gaipov.talim_crm.dto.TeacherDto;
import com.gaipov.talim_crm.service.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/v1/auth")
@AllArgsConstructor
public class AuthController {
    private final AuthService service;

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("auth", new AuthDto());
        return "newUser";
    }

    @PostMapping("/register")
    @ResponseBody
    public ResponseEntity<AuthDto> register(@RequestBody AuthDto dto) {
        return ResponseEntity.ok(service.registerNewUser(dto));
    }

    @GetMapping("/listPage")
    public String showList(Model model) {
        List<AuthDto> authDtos = service.listOfUsers();
        model.addAttribute("users", authDtos); // Ensure "users" matches template
        return "registers";
    }

    @GetMapping("/list")
    @ResponseBody
    public ResponseEntity<List<AuthDto>> getAll() {
        return ResponseEntity.ok(service.listOfUsers());
    }

    @GetMapping("/users")
    @ResponseBody
    public ResponseEntity<List<AuthDto>> getUsers() {
        return ResponseEntity.ok(service.listOfUsers());
    }

    @GetMapping("/reception")
    public String receptionPage(Model model) {
        // Redirect old path to new login page to avoid exposing system content pre-auth
        return "redirect:/v1/auth/loginPage";
    }

    @GetMapping("/loginPage")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/system-users")
    public String systemUsersPage(Model model) {
        return "system_users";
    }

    @PostMapping("/login")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> credentials, HttpSession session) {
        String username = credentials.get("username");
        String password = credentials.get("password");

        return service.authenticate(username, password)
                .filter(entity -> entity.getRoles() != null && entity.getRoles().name().equals("ADMIN"))
                .map(entity -> {
                    session.setAttribute("isLoggedIn", true);
                    session.setAttribute("username", username);
                    session.setAttribute("role", "SUPER_ADMIN");
                    return ResponseEntity.ok(Map.of(
                            "success", true,
                            "message", "Login successful",
                            "user", Map.of(
                                    "username", username,
                                    "role", "SUPER_ADMIN"
                            )
                    ));
                })
                .orElseGet(() -> ResponseEntity.badRequest().body(Map.of(
                        "success", false,
                        "message", "Invalid credentials"
                )));
    }

    @PostMapping("/logout")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok(Map.of(
            "success", true,
            "message", "Logged out successfully"
        ));
    }

}
