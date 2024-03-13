package com.cdd.controller;

import com.alibaba.fastjson2.JSONObject;
import com.cdd.service.websocket.WebSocketServer;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
public class SendResultController {
    @PostMapping("/result/send")
    public void getResultController(@RequestParam MultiValueMap<String, String> data) {
        String result = data.getFirst("result");
        int userId = Integer.parseInt(Objects.requireNonNull(data.getFirst("user_id")));
        JSONObject resp = new JSONObject();
        if("success".equals(result)) {
            resp.put("event", "detect-finished");
            resp.put("detection_time", data.getFirst("detection_time"));
        } else {
            resp.put("event", "detect-error");
        }
        // 避免 websocket 关闭时还发送
        WebSocketServer.userSocketMap.computeIfPresent(userId, (key, value) -> {
            value.sendMessage(resp.toJSONString());
            return value;
        });
    }
}
