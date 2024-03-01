package com.cdd.service;

import com.cdd.utils.OSSUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class GetOSSPolicyService {
    @Autowired
    OSSUtils ossUtils;
    public Map<String, String> getOSSPolicy(String repoName) {
        return ossUtils.getOSSPolicy(repoName);
    }
}
