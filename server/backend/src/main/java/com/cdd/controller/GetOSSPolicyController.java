package com.cdd.controller;

import com.cdd.service.GetOSSPolicyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class GetOSSPolicyController {
    @Autowired
    GetOSSPolicyService getOSSPolicyService;
    @PostMapping("/oss/policy")
    public Map<String, String> getOSSPolicy(@RequestParam Map<String, String> requestMap) {
        return getOSSPolicyService.getOSSPolicy(requestMap.get("repoName"));
    }
}
