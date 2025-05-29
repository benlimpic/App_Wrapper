package com.authentication.demo.Controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.authentication.demo.Config.AppProperties;

@ControllerAdvice
public class GlobalModelAdvice {

    private final AppProperties appProperties;

    public GlobalModelAdvice(AppProperties appProperties) {
        this.appProperties = appProperties;
    }

    @ModelAttribute
    public void addBaseUrl(Model model) {
        model.addAttribute("baseUrl", appProperties.getBaseUrl());
    }
}