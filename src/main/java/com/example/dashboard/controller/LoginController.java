package com.example.dashboard.controller;

import com.example.dashboard.LoginDTO;
import com.example.dashboard.RegisterDTO;
import com.example.dashboard.repository.RoleRepository;
import com.example.dashboard.repository.UserRepository;
import com.example.dashboard.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

    @Autowired
    LoginService loginService;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody RegisterDTO registerDTO){
        return loginService.registerUser(registerDTO);

    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO){
        return loginService.loginUser(loginDTO);
    }
}
