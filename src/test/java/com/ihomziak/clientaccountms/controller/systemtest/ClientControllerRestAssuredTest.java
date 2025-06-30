package com.ihomziak.clientaccountms.controller.systemtest;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Import;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.ihomziak.clientaccountms.controller.security.config.TestSecurityConfig;

@SpringBootTest(
	webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
	properties = {
		"spring.kafka.bootstrap-servers=localhost:9092",  // required dummy value
		"spring.kafka.listener.auto-startup=false",        // disables listener containers
		"eureka.client.enabled=false",				// disables Eureka client
	})
@Import(TestSecurityConfig.class)
@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ClientControllerRestAssuredTest {

	@Container
	@ServiceConnection
	private static MySQLContainer<?> mySQLContainer = new MySQLContainer<>("mysql:8.4.0");
}
