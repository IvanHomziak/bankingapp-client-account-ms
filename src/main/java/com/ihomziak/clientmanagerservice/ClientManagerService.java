package com.ihomziak.clientmanagerservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class ClientManagerService {

	public static void main(String[] args) {
		SpringApplication.run(ClientManagerService.class, args);
	}

}
