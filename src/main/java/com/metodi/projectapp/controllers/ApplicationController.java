package com.metodi.projectapp.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class ApplicationController {

    @Value("${app.info.name}")
    private String appName;

    @Value("${app.info.version}")
    private Integer versionNumber;

    @Value("${app.info.active}")
    private Boolean isActive;

    @GetMapping("/app/info")
    public Map<String, Object> getAppInfo() {
        Map<String, Object> appInfoMap = new HashMap<>();

        appInfoMap.put("appName", appName);
        appInfoMap.put("versionNumber", versionNumber);
        appInfoMap.put("isActive", isActive);

        return appInfoMap;
    }
}
