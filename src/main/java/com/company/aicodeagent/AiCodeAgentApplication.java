package com.company.aicodeagent;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class AiCodeAgentApplication {

    public static void main(String[] args) {
        SpringApplication.run(
                AiCodeAgentApplication.class,
                args);
    }

    @Bean
    CommandLineRunner test(
            @Value("${spring.datasource.url:NOT_FOUND}") String url,
            @Value("${spring.datasource.username:NOT_FOUND}") String username,
            @Value("${spring.datasource.password:NOT_FOUND}") String password
    ) {

        return args -> {

            System.out.println("URL=" + url);
            System.out.println("USER=" + username);
            System.out.println("PASS=" + password);

        };
    }
}