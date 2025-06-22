package com.ihomziak.clientaccountms.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
public class ClientControllerIntegrationTestcontainersTest {

	@Container
	private static MySQLContainer mySQLContainer = new MySQLContainer("mysql:8.4.0")
		.withDatabaseName("clients_db")
		.withUsername("root")
		.withPassword("admin1234");

	@DynamicPropertySource
	private static void overrideMySQLContainerProperties(DynamicPropertyRegistry registry) {
		// This method can be used to override any properties of the MySQL container if needed.
		// For example, you can set environment variables or change the database name.
		registry.add("spring.datasource.url", mySQLContainer::getJdbcUrl);
		registry.add("spring.datasource.username", mySQLContainer::getUsername);
		registry.add("spring.datasource.password", mySQLContainer::getPassword);
	}

	@Test
	@DisplayName("The MySQL container is created, and is running")
	public void testTestContainerIsRunning() {
		// This test will run with MySQL container
		// Implement the test logic here
		// For example, you can use TestRestTemplate to send a request to the API
		// and verify the response.
		// Note: The actual implementation of the test logic is not provided here.
		Assertions.assertTrue(mySQLContainer.isCreated(), "The MySQL container has not been created"); // Placeholder assertion
		Assertions.assertTrue(mySQLContainer.isRunning(), "The MySQL container has not been running"); // Placeholder assertion
	}
}
