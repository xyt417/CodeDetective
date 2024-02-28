package com.cdd;

import com.cdd.utils.JwtUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class JwtUtilsTest {
    @Autowired
    private JwtUtils jwtUtils;
    @Test
    public void testPrintInfo() {
        jwtUtils.printConfig();
    }
    @Test
    public void testGenerateAndParse() {
        String token = jwtUtils.generateToken(String.valueOf(123));
        System.out.println(token);
        System.out.println(jwtUtils.getPayloadByToken("eyJ0eXBlIjoiSldUIiwiYWxnIjoiSFM1MTIifQ.eyJpc3MiOiJDb2RlRGV0ZWN0aXZlIiwiaWF0IjoxNzA4ODQ2NTg3LCJleHAiOjE3MDAwMzYxODcsInN1YiI6IjEyMyIsImp0aSI6ImI1MGZjYzc1LTNkYmQtNDY1ZC1iZmI4LTU2MDhjMmMwMTRhMCJ9.p7nXYVluEEDjX-r70CZV3mzReJnrV9rJnFcStNSxKA9NT8GrK-GGG717-CLXRZy9AAmIYwOHbSXgfnaZOZj8XA"));
    }
}
