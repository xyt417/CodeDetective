package com.cdd.service.websocket;


import com.alibaba.fastjson2.JSONObject;
import com.cdd.mapper.UserMapper;
import com.cdd.pojo.User;
import com.cdd.utils.JwtUtils;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

@Component
@ServerEndpoint("/websocket/{token}") // 注意不能以 / 结尾 (以 / 结尾表示目录，这里要表示端点，加 / 会导致异常)
public class WebSocketServer {
    // CopyOnWriteArraySet 是一个线程安全的集合，采用写时复制，写完替换原有数据(替换操作是原子的)，适用于读多写少的场景
    public static final ConcurrentHashMap<Integer, WebSocketServer> userSocketMap = new ConcurrentHashMap<>(); // 线程安全的 HashMap
    // 静态全局变量
    private static UserMapper userMapper;
    private static JwtUtils jwtUtils;
    private static RestTemplate restTemplate;

    // 每次生成实例时注入 (会覆盖注入, 但不影响)
    @Autowired
    public void setUserMapper(UserMapper userMapper) {
        WebSocketServer.userMapper = userMapper;
    }
    @Autowired
    public void setJwtUtils(JwtUtils jwtUtils) {
        WebSocketServer.jwtUtils = jwtUtils;
    }
    @Autowired
    public void setRestTemplate(RestTemplate restTemplate) {
        WebSocketServer.restTemplate = restTemplate;
    }

    private User user;
    private Session session;

    private static final String startDetectionUrl = "http://127.0.0.1:3001/detection/start";

    // WebSocket连接打开时，WebSocket API会创建一个新的session对象，并自动传递给@OnOpen注解的方法。
    @OnOpen
    public void onOpen(Session session, @PathParam("token") String token) throws IOException {
        // 建立连接
        this.session = session;
        Integer userId = Integer.parseInt(jwtUtils.getPayloadByToken(token).getSubject());
        this.user = userMapper.selectById(userId);
        if(this.user != null) { // 用户存在，token 正确
            userSocketMap.put(userId, this); // 根据 userId 存放
            System.out.println("Websocket Connection opened: " + user);
        } else {
            this.session.close();
        }
    }

    @OnMessage
    public void onMessage(String message, Session session) throws IOException {
        JSONObject dataReceived = JSONObject.parseObject(message);
        JSONObject resp = new JSONObject();
        System.out.println("Websocket Received message: " + message);
        if(dataReceived.getString("event").equals("detect-start")) {
            resp.put("event", "detect-start");
            session.getBasicRemote().sendText(resp.toJSONString());
            MultiValueMap<String, String> data = new LinkedMultiValueMap<>();
            data.add("repo_name", dataReceived.getString("repo_name"));
            restTemplate.postForObject(startDetectionUrl, data, String.class);
        }
    }
}
