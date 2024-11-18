package com.mad.MAD;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "Mobile App Dev Project", version = "2.0", description = "MAD"))
public class MadApplication {
	public static void main(String[] args) {
		SpringApplication.run(MadApplication.class, args);
	}

}
