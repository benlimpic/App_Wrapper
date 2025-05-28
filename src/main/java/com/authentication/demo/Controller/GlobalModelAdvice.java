package com.authentication.demo.Controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalModelAdvice {

    @Value("${app.base-url}")
    private String baseUrl;

    @ModelAttribute
    public void addBaseUrl(Model model) {
        model.addAttribute("baseUrl", baseUrl);
    }
}