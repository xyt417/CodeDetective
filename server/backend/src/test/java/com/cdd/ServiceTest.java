package com.cdd;

import com.cdd.service.ListRepositoriesService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ServiceTest {
    @Autowired
    private ListRepositoriesService listRepositoriesService;
    @Test
    public void testListRepositoriesService() {
        System.out.println(listRepositoriesService.listRepositories("123"));
    }
}
