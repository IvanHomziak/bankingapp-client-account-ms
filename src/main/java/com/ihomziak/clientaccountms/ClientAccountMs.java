package com.ihomziak.clientaccountms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@OpenAPIDefinition(
	info = @Info(
		title = "Client Account Service API",
		version = "1.0",
		description = "API for managing clients and accounts"
	)
)
@SpringBootApplication
@EnableDiscoveryClient
@EnableAspectJAutoProxy
public class ClientAccountMs {

	public static void main(String[] args) {
		SpringApplication.run(ClientAccountMs.class, args);
	}
}