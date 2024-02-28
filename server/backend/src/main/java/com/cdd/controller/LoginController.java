package com.cdd.controller;

import com.cdd.mapper.UserMapper;
import com.cdd.service.LoginService;
import org.apache.tomcat.util.log.SystemLogHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class LoginController {
    @Autowired
    LoginService loginService;

    @PostMapping("/login")
    public Map<String, String> addUser(@RequestParam Map<String, String> data) {
        String username = data.get("username");
        String password = data.get("password");
        return loginService.getToken(username, password);
    }

}
