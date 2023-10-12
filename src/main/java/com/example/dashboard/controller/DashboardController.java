package com.example.dashboard.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DashboardController {

    @RequestMapping("/dashboard")
    public String loginController(){
        return "Hello world!";
    }
}
