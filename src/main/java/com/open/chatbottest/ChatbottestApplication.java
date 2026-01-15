package com.open.chatbottest;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;

@SpringBootApplication
public class ChatbottestApplication {

	public static void main(String[] args) {
		SpringApplication.run(ChatbottestApplication.class, args);
	}

	@Bean
	ApplicationRunner beanDebugger(ApplicationContext context) {
		ApplicationRunner applicationRunner = args -> {
			Map<String, Object> beans = new TreeMap<>(context.getBeansOfType(Object.class));
			// 🔴 PUT BREAKPOINT HERE
			System.out.println("Total beans: " + beans.size());
		};
		return applicationRunner;
	}



}
