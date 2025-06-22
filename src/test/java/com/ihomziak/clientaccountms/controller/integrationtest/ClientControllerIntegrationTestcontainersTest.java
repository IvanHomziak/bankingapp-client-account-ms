package com.ihomziak.clientaccountms.controller.integrationtest;

import static com.ihomziak.clientaccountms.util.constants.Endpoints.ClientEndpoints.ADD_CLIENT;
import static com.ihomziak.clientaccountms.util.constants.Endpoints.ClientEndpoints.API_CLIENT_V1;

import java.util.Arrays;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.ihomziak.clientaccountms.controller.integrationtest.config.TestSecurityConfig;
import com.ihomziak.clientaccountms.dto.ClientResponseDTO;

import net.datafaker.Faker;

@SpringBootTest(
	webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
	properties = {
		"spring.kafka.bootstrap-servers=localhost:9092",  // required dummy value
		"spring.kafka.listener.auto-startup=false",        // disables listener containers
		"eureka.client.enabled=false",				// disables Eureka client
	})
@Import(TestSecurityConfig.class)
@Testcontainers
public class ClientControllerIntegrationTestcontainersTest {

	@MockitoBean
	private Faker faker;

	@Autowired
	private TestRestTemplate testRestTemplate;

	@Container
	@ServiceConnection
	private static MySQLContainer mySQLContainer = new MySQLContainer("mysql:8.4.0")
		.withDatabaseName("clients_db")
		.withUsername("root")
		.withPassword("admin1234");

//	@DynamicPropertySource
//	private static void overrideMySQLContainerProperties(DynamicPropertyRegistry registry) {
//		// This method can be used to override any properties of the MySQL container if needed.
//		// For example, you can set environment variables or change the database name.
//		registry.add("spring.datasource.url", mySQLContainer::getJdbcUrl);
//		registry.add("spring.datasource.username", mySQLContainer::getUsername);
//		registry.add("spring.datasource.password", mySQLContainer::getPassword);
//	}

	@Test
	@DisplayName("The MySQL container is created, and is running")
	public void testTestContainerIsRunning() {
		Assertions.assertTrue(mySQLContainer.isCreated(), "The MySQL container has not been created"); // Placeholder assertion
		Assertions.assertTrue(mySQLContainer.isRunning(), "The MySQL container has not been running"); // Placeholder assertion
	}

	@BeforeEach
	public void setUp() {
		faker = new Faker();
	}

	@Test
	@DisplayName("Client can be created with valid details")
	public void testCreateClient_whenValidDetailsProvided_shouldReturnUserDetails() throws JSONException {
		// Arrange
		JSONObject userDetailsRequestObject = new JSONObject();
		userDetailsRequestObject.put("firstName", faker.name().firstName());
		userDetailsRequestObject.put("lastName", faker.name().lastName());
		userDetailsRequestObject.put("dateOfBirth", "1990-01-01");
		userDetailsRequestObject.put("taxNumber", "1234567890");
		userDetailsRequestObject.put("email", faker.internet().emailAddress());
		userDetailsRequestObject.put("phoneNumber", faker.phoneNumber().phoneNumber());
		userDetailsRequestObject.put("address", "123 Main St, Springfield");

		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		httpHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

		HttpEntity<String> requestEntity = new HttpEntity<>(userDetailsRequestObject.toString(), httpHeaders);

		// Act
		ResponseEntity<ClientResponseDTO> createdUserDetailsEntity = testRestTemplate.postForEntity(
			API_CLIENT_V1 + ADD_CLIENT,
			requestEntity,
			ClientResponseDTO.class);

		ClientResponseDTO createdUserDetails = createdUserDetailsEntity.getBody();

		// Assert
		Assertions.assertNotNull(createdUserDetails, "Created user details should not be null");
		Assertions.assertEquals(HttpStatus.CREATED, createdUserDetailsEntity.getStatusCode(), "Response status should be CREATED");
		Assertions.assertNotNull(createdUserDetails.getUUID(), "Created user UUID should not be null");
		Assertions.assertEquals(userDetailsRequestObject.get("firstName"), createdUserDetails.getFirstName(), "First name should match");
		Assertions.assertEquals(userDetailsRequestObject.get("lastName"), createdUserDetails.getLastName(), "Last name should match");
		Assertions.assertEquals(userDetailsRequestObject.get("dateOfBirth"), createdUserDetails.getDateOfBirth(), "Date of birth should match");
		Assertions.assertEquals(userDetailsRequestObject.get("taxNumber"), createdUserDetails.getTaxNumber(), "Tax number should match");
		Assertions.assertEquals(userDetailsRequestObject.get("email"), createdUserDetails.getEmail(), "Email should match");
		Assertions.assertEquals(userDetailsRequestObject.get("phoneNumber"), createdUserDetails.getPhoneNumber(), "Phone number should match");
		Assertions.assertEquals(userDetailsRequestObject.get("address"), createdUserDetails.getAddress(), "Address should match");

	}
}