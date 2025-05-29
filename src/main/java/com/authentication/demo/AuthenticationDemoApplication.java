package com.authentication.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;

import com.authentication.demo.Config.AppProperties;

@EnableConfigurationProperties(AppProperties.class)
@SpringBootApplication
@ComponentScan(basePackages = "com.authentication.demo")
public class AuthenticationDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthenticationDemoApplication.class, args);
	}
}
