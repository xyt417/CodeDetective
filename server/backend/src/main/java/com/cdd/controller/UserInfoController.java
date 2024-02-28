package com.cdd.controller;

import com.cdd.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class UserInfoController {
    @Autowired
    private UserInfoService userInfoService;

    @GetMapping("/userinfo")
    public Map<String, String> getUserInfo(){
        return userInfoService.getUserInfo();
    }
}
