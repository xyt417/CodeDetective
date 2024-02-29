package com.cdd;

import com.cdd.service.CreateRepositoryService;
import com.cdd.service.ListRepositoriesService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

@SpringBootTest
public class OSSUtilsTest {
    @Autowired
    CreateRepositoryService createRepositoryService;
    @Autowired
    ListRepositoriesService listRepositoriesService;
    @Test
    public void testCreateDir() {
        Map<String, String> map = createRepositoryService.createRepository("wowTest", "description whatever");
        System.out.println(map.get("message"));
    }
}
