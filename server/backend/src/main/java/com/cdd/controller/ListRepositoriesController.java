package com.cdd.controller;

import com.cdd.pojo.Repository;
import com.cdd.service.ListRepositoriesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ListRepositoriesController {
    @Autowired
    private ListRepositoriesService listRepositoriesService;
    @GetMapping("/repository/list/{username}")
    public List<Repository> listRepositories(@PathVariable String username) {
        return listRepositoriesService.listRepositories(username);
    }
}
