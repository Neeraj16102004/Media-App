package com.example.media_app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class MediaAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(MediaAppApplication.class, args);
	}

}
