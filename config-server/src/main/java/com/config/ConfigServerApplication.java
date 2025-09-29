package com.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@SpringBootApplication
@EnableConfigServer
public class ConfigServerApplication implements CommandLineRunner  {

	public static void main(String[] args) {
		System.out.println("✅✅✅ CONFIG-SERVER IS RUNNING ✅✅✅");
		SpringApplication.run(ConfigServerApplication.class, args);
	}

	@Override
	public void run(String... args) {
		System.out.println("✅✅✅ CONFIG-SERVER is up and ready! ✅✅✅");
	}

}
