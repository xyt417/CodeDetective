package com.cdd.service;

import com.cdd.utils.OSSUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ListSubmissionFilesService {
    @Autowired
    OSSUtils ossUtils;
    public List<String> listFiles(String repoName, String submissionId) {
        return ossUtils.listObjectsAt("Results/" + repoName + "/files/" + submissionId, false);
    }
}
