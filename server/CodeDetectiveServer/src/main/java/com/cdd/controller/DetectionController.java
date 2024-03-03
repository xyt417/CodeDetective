package com.cdd.controller;

import com.cdd.service.DetectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class DetectionController {
    @Autowired
    private DetectionService detectionService;
    @PostMapping("/detection/start")
    public void detection(@RequestParam MultiValueMap<String, String> data) throws IOException {
        String repoName = data.getFirst("repo_name");
        detectionService.detection(repoName);
    }
}
