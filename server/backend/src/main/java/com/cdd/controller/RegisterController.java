package com.cdd.controller;

import com.cdd.mapper.UserMapper;
import com.cdd.service.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class RegisterController {
    @Autowired
    RegisterService registerService;

    @PostMapping("/register")
    public Map<String, String> register(@RequestParam Map<String, String> data) {
        String username = data.get("username");
        String password = data.get("password");
        String confirmedPassword = data.get("confirmed_password");
        return registerService.addUser(username, password, confirmedPassword);
    }
}
