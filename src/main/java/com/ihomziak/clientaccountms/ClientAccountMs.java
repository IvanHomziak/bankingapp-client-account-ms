package com.ihomziak.clientaccountms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class ClientAccountMs {

	public static void main(String[] args) {
		SpringApplication.run(ClientAccountMs.class, args);
	}

}