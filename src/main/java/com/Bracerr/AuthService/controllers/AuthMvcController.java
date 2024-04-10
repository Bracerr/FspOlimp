package com.Bracerr.AuthService.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthMvcController {

    @GetMapping("/registration")
    public String registrationPage(){
        return "registration";
    }

    @GetMapping("/login")
    public String loginPage() { return "login"; }

    @GetMapping("/manage")
    public String managePage() { return "manage"; }

    @GetMapping("/errorToken")
    public String errorTokenPage() { return "errorToken"; }

    @GetMapping()
    public String mainPage() { return "index"; }

}
