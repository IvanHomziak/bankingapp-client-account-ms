package com.ihomziak.clientaccountms.controller;

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
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.ihomziak.clientaccountms.dto.ClientResponseDTO;

import net.datafaker.Faker;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
//@TestPropertySource(locations = "/application-test.yaml")
public class ClientControllerIntegrationTest {


	@MockitoBean
	private Faker faker;

	@Autowired
	private TestRestTemplate testRestTemplate;

	@BeforeEach
	public void setUp() {
		faker = new Faker();
	}

	@Test
	@DisplayName("Client can be created with valid details")
	public void testCreateClient_whenValidDetailsProvided_shouldReturnUserDetails() throws JSONException {
		// Arrange
		JSONObject userDetailsRequestObject = new JSONObject();
		userDetailsRequestObject.put("firstName", "John");
		userDetailsRequestObject.put("lastName", "Doe");
		userDetailsRequestObject.put("dateOfBirth", "1990-01-01");
		userDetailsRequestObject.put("taxNumber", "1234567890");
		userDetailsRequestObject.put("email", "");
		userDetailsRequestObject.put("phoneNumber", "1234567890");
		userDetailsRequestObject.put("address", "123 Main St, Springfield");

		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		httpHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		httpHeaders.set("Authorization", "Bearer " + faker.internet().uuid());

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
		Assertions.assertEquals("John", createdUserDetails.getFirstName(), "First name should match");
		Assertions.assertEquals("Doe", createdUserDetails.getLastName(), "Last name should match");
		Assertions.assertEquals("1990-01-01", createdUserDetails.getDateOfBirth(), "Date of birth should match");
		Assertions.assertEquals("1234567890", createdUserDetails.getTaxNumber(), "Tax number should match");
		Assertions.assertEquals("1234567890", createdUserDetails.getPhoneNumber(), "Phone number should match");
		Assertions.assertEquals("123 Main St, Springfield", createdUserDetails.getAddress(), "Address should match");

	}

}
