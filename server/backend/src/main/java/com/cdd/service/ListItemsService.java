package com.cdd.service;

import com.cdd.utils.OSSUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Service
public class ListItemsService {
    @Autowired
    private OSSUtils ossUtils;
    public List<Map<String, String>> listItems(String repoName) {
        List<Map<String, String>> items = new LinkedList<>();
        List<String> userDirList = ossUtils.listObjectsAt(ossUtils.getCodeDir() + repoName, true);
        for(String userDir : userDirList) {
            if(userDir.endsWith("/")) userDir = userDir.substring(0, userDir.length() - 1);
            String username = userDir.substring(userDir.lastIndexOf("/") + 1);
            List<String> submissionList = ossUtils.listObjectsAt(userDir, false);
            for(String submission : submissionList) {
                String submissionName = submission.substring(submission.lastIndexOf("/") + 1);
                Map<String, String> item = Map.of("username", username, "submissionName", submissionName);
                items.add(item);
            }
        }
        return items;
    }
}
