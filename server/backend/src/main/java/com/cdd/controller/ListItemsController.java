package com.cdd.controller;

import com.cdd.service.ListItemsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class ListItemsController {
    @Autowired
    private ListItemsService listItemsService;
    @PostMapping("/items/list")
    public List<Map<String, String>> listItems(@RequestParam Map<String, String> requestMap) {
        return listItemsService.listItems(requestMap.get("repo_name"));
    }
}
