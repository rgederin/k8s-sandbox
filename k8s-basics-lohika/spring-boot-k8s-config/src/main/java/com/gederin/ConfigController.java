package com.gederin;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;

import lombok.Data;

@RestController
@RequestMapping("/api")
public class ConfigController {

    @Value("${database.host}")
    private String host;

    @Value("${database.port}")
    private String port;

    @GetMapping("hello")
    public String hello() {
        return "Hello from spring boot application demostrated k8s configs";
    }

    @GetMapping("ip")
    public String ip() throws UnknownHostException {
        return "Ip address: " + InetAddress.getLocalHost();
    }

    @GetMapping("secrets")
    public SecretsResponse secrets() {
        SecretsResponse secretsResponse = new SecretsResponse();

        secretsResponse.username = System.getenv().getOrDefault("SECRETS_USERNAME", "default-username");
        secretsResponse.password = System.getenv().getOrDefault("SECRETS_PASSWORD", "default-password");

        return secretsResponse;
    }

    @GetMapping("configMap")
    public ConfigMapResponse configMap() {
        ConfigMapResponse configMapResponse = new ConfigMapResponse();

        configMapResponse.host = host;
        configMapResponse.port = port;

        return configMapResponse;
    }

    @Data
    private class SecretsResponse {
        private String username;
        private String password;
    }

    @Data
    private class ConfigMapResponse {
        private String host;
        private String port;
    }

    private String getIp() {
        try {
            URL url = new URL("http://169.254.169.254/latest/meta-data/public-ipv4");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            con.getResponseCode();

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));

            String result = in.readLine();

            in.close();
            con.disconnect();

            return result;
        } catch (Exception ex) {
            // LOG.error("Could not obtain ip address, root cause is: {} ", ex.getMessage());
            return "no ip";
        }
    }
}

