package com.cdd;

import com.cdd.service.CreateRepositoryService;
import com.cdd.service.ListRepositoriesService;
import com.cdd.utils.OSSUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

import java.util.List;
import java.util.Map;

@SpringBootTest
public class OSSUtilsTest {
    // 当在测试环境中时，不实例化ServerEndpointExporter
    @MockBean
    private ServerEndpointExporter serverEndpointExporter; // 不加会出现无法创建ServerEndpointExporter实例的异常
    @Autowired
    CreateRepositoryService createRepositoryService;
    @Autowired
    ListRepositoriesService listRepositoriesService;
    @Autowired
    OSSUtils ossUtils;
    @Test
    public void testCreateDir() {
        Map<String, String> map = createRepositoryService.createRepository("wowTest", "description whatever");
        System.out.println(map.get("message"));
    }

    @Test
    public void testListFiles() {
        List<String> list = ossUtils.listObjectsAt("Results", false);
        for(String file : list) {
            System.out.println(file);
        }
    }
}
