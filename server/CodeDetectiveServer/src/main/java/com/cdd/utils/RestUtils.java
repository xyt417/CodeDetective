package com.cdd.utils;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Component
public class RestUtils {
    @Autowired
    private RestTemplate restTemplate;
    public static String BackendGetResultUrl = "http://127.0.0.1:3000/result/send";

    public void postStrs(String url, MultiValueMap<String, String> data) {
        restTemplate.postForObject(url, data, String.class);
    }
}
