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

import java.util.List;
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
        return "reception";
    }

    @GetMapping("/reception")
    public String showReception(Model model) {
        // Preload users for initial render; client script will refresh as needed
        List<AuthDto> authDtos = service.listOfUsers();
        model.addAttribute("users", authDtos);
        model.addAttribute("auth", new AuthDto());
        return "reception";
    }

    @GetMapping("/list")
    @ResponseBody
    public ResponseEntity<List<AuthDto>> getAll() {
        return ResponseEntity.ok(service.listOfUsers());
    }

}
