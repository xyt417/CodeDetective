package com.cdd.service;

import com.cdd.pojo.User;
import com.cdd.utils.JwtUtils;
import com.cdd.utils.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class LoginService {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtUtils jwtUtils;

    public Map<String, String> getToken(String username, String password) {
        Map<String, String> res = new HashMap<>();
        // 前端显示信息
        if (username == null) {
            return Map.of("message", "username is null");
        } else if(password == null) {
            return Map.of("message", "password is null");
        }
        if (username.isEmpty()) {
            return Map.of("message", "用户名不能为空");
        } else if(password.isEmpty()) {
            return Map.of("message", "密码不能为空");
        }

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(authenticationToken);
        } catch (Exception e) {
            return Map.of("message", "用户名或密码错误");
        }
        User user = ((UserDetailsImpl) authentication.getPrincipal()).getUser();

        res.put("message", "success");
        res.put("token", jwtUtils.generateToken(user.getId().toString()));
        return res;
    }
}
