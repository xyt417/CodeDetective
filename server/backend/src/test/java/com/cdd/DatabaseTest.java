package com.cdd;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cdd.BackendApplication;
import com.cdd.mapper.UserMapper;
import com.cdd.pojo.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class DatabaseTest {

    @Autowired
    private UserMapper userMapper;

    @Test
    public void testSelectUserByUsername() {
        String username = "usernam";
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        User user = userMapper.selectOne(queryWrapper);
        System.out.println(user);
    }
}