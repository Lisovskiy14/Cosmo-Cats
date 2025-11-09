package com.example.cosmocats;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy
public class CosmocatsApplication {

	public static void main(String[] args) {
		SpringApplication.run(CosmocatsApplication.class, args);
	}

}
