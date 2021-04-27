package com.gederin;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.Data;

@RestController
@RequestMapping("/api")
public class ConfigController {

    @Value("${secrets.username}")
    private String secretsUsername;

    @Value("${secrets.password}")
    private String secretsPassword;

    @GetMapping("hello")
    public String hello() {
        return "Hello from spring boot application demostrated k8s configs";
    }

    @GetMapping("secrets")
    public SecretsResponse secrets() {
        SecretsResponse secretsResponse = new SecretsResponse();

        secretsResponse.username = secretsUsername;
        secretsResponse.password = secretsPassword;

        return secretsResponse;
    }

    @Data
    private class SecretsResponse {
        private String username;
        private String password;
    }
}

