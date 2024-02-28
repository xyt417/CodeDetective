package com.cdd.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cdd.mapper.UserMapper;
import com.cdd.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class RegisterService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public Map<String, String> addUser(String username, String password, String confirmedPassword) {
        Map<String, String> res = new HashMap<>();
        // 前端显示信息
        if (username == null) {
            res.put("message", "username is null");
            return res;
        }
        if (password == null) {
            res.put("message", "password is null");
            return res;
        }
        if (confirmedPassword == null) {
            res.put("message", "confirmedPassword is null");
            return res;
        }

        if (username.isEmpty()) {
            res.put("message", "用户名不能为空");
            return res;
        }
        if (password.isEmpty()) {
            res.put("message", "密码不能为空");
            return res;
        }
        if (confirmedPassword.isEmpty() || !password.equals(confirmedPassword)) {
            res.put("message", "输入密码不一致");
            return res;
        }

        if (username.length() > 100) {
            res.put("message", "用户名过长");
            return res;
        }
        if (password.length() > 100) {
            res.put("message", "密码过长");
            return res;
        }

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        User user = userMapper.selectOne(queryWrapper);
        if (user != null) {
            res.put("message", "用户名已存在");
            return res;
        }

        String encodedPassword = passwordEncoder.encode(password);

        user = new User(null, username, encodedPassword, new Date(), null);
        userMapper.insert(user);

        res.put("message", "success");
        return res;
    }
}
