package com.anna.power.desafio_java_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class DesafioJavaBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(DesafioJavaBackendApplication.class, args);
	}

}
