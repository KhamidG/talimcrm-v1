package com.gaipov.talim_crm.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@AllArgsConstructor
public class AuthController1 {
    @GetMapping("/login")
    public String login(){
        return "loginPage";
    }
}
