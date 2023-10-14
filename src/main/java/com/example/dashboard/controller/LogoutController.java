package com.example.dashboard.controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class LogoutController {

    @PostMapping("/logout")
    public ResponseEntity<String> logout() {

        SecurityContextHolder.clearContext();
        return ResponseEntity.status(HttpStatus.OK).body("Logging out!");
    }
}
