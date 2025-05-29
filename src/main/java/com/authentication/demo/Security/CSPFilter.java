package com.authentication.demo.Security;


import java.io.IOException;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletResponse;

@Profile("demo")
@Component
public class CSPFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletResponse res = (HttpServletResponse) response;

        // Allow iframe embedding only from self and subdomains of benlimpic.info
        res.setHeader("Content-Security-Policy", "frame-ancestors 'self' https://portfolio.benlimpic.info https://shelved-demo-app.benlimpic.info");


        chain.doFilter(request, response);
    }
}