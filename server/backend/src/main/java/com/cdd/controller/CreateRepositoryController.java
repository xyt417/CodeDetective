package com.cdd.controller;

import com.cdd.service.CreateRepositoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class CreateRepositoryController {
    @Autowired
    CreateRepositoryService createRepositoryService;
    @GetMapping("/repository/create/{name}/{description}")
    public Map<String, String> createRepository(@PathVariable String name, @PathVariable String description) {
        return createRepositoryService.createRepository(name, description);
    }
}
