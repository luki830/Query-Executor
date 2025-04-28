package com.example.queryexecutor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class EnvCheckRunner implements CommandLineRunner {

    @Value("${DB_HOST}")
    private String dbHost;

    @Value("${DB_PORT}")
    private String dbPort;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("DB Host: " + dbHost);
        System.out.println("DB Port: " + dbPort);
    }
}