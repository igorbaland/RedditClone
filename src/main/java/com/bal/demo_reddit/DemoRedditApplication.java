package com.bal.demo_reddit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class DemoRedditApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoRedditApplication.class, args);
	}

}
