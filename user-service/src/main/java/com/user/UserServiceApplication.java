package com.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class UserServiceApplication {

    // ANSI escape codes for colors
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_GREEN = "\u001B[32m";

    public static void main(String[] args) {
        SpringApplication.run(UserServiceApplication.class, args);
        System.out.println(ANSI_GREEN + "✅✅✅ USER SERVICE IS RUNNING SUCCESSFULLY! ✅✅✅" + ANSI_RESET);
    }

}
