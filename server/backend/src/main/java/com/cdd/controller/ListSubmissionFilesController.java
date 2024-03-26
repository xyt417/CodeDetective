package com.cdd.controller;

import com.cdd.service.ListSubmissionFilesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class ListSubmissionFilesController {
    @Autowired
    ListSubmissionFilesService listSubmissionFilesService;
    @PostMapping("/files")
    List<String> listFiles(@RequestParam Map<String, String> requestMap) {
        String repoName = requestMap.get("repo_name");
        String submissionId = requestMap.get("submission_id");
        return listSubmissionFilesService.listFiles(repoName, submissionId);
    }
}
