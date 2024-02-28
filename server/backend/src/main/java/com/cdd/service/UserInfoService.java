package com.cdd.service;

import com.cdd.pojo.User;
import com.cdd.utils.UserUtils;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserInfoService {
    public Map<String, String> getUserInfo() {
        User user = UserUtils.GetUserFromContext();
        // 改成 put, Map.of() 中不允许值为 null
         Map<String, String> map = new HashMap<>();
         map.put("message", "success");
         map.put("id", user.getId().toString());
         map.put("username", user.getUsername());
         map.put("createTime", user.getCreateTime().toString());
         map.put("photo", user.getPhoto());
         return map;
    }
}
