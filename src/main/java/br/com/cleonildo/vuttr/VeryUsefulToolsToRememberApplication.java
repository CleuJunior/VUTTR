package br.com.cleonildo.vuttr;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "Very Useful Tools to Remember", version = "1"))
public class VeryUsefulToolsToRememberApplication {

	public static void main(String[] args) {
		SpringApplication.run(VeryUsefulToolsToRememberApplication.class, args);
	}

}
