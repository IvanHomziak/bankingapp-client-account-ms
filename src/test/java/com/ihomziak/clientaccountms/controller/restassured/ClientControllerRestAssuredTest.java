package com.ihomziak.clientaccountms.controller.restassured;

import static com.ihomziak.clientaccountms.util.constants.Endpoints.ClientEndpoints.ADD_CLIENT;
import static com.ihomziak.clientaccountms.util.constants.Endpoints.ClientEndpoints.API_CLIENT_V1;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.lessThan;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.assertj.core.api.AutoCloseableSoftAssertions;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.ihomziak.clientaccountms.controller.security.config.TestSecurityConfig;
import com.ihomziak.clientaccountms.dto.ClientRequestDTO;
import com.ihomziak.clientaccountms.dto.ClientResponseDTO;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.parsing.Parser;
import net.datafaker.Faker;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = {
                "spring.kafka.bootstrap-servers=localhost:9092",  // required dummy value
                "spring.kafka.listener.auto-startup=false",        // disables listener containers
                "eureka.client.enabled=false",                // disables Eureka client
        })
@Import(TestSecurityConfig.class)
@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ClientControllerRestAssuredTest {

    @Container
    @ServiceConnection
    private static MySQLContainer<?> mySQLContainer = new MySQLContainer<>("mysql:8.4.0")
            .withDatabaseName("clients_db")
            .withUsername("root")
            .withPassword("admin1234");

    private Faker faker;

    @LocalServerPort
    private int port;

    private final RequestLoggingFilter requestLoggingFilter = new RequestLoggingFilter(LogDetail.ALL);
    private final ResponseLoggingFilter responseLoggingFilter = new ResponseLoggingFilter(LogDetail.ALL);

    @BeforeAll
    void setUp() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
        RestAssured.filters(requestLoggingFilter, responseLoggingFilter);
        RestAssured.defaultParser = Parser.JSON;
        RestAssured.requestSpecification = new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .build();
        RestAssured.responseSpecification = new ResponseSpecBuilder()
                .expectResponseTime(lessThan(2000L)) // Response time should be less than 2 seconds
                .build();
    }

    @BeforeEach
    void init() {
        faker = new Faker();
    }

    @Order(1)
    @Test
    public void testContainerIsRunning() {
        Assertions.assertTrue(mySQLContainer.isRunning());
    }

    @Order(2)
    @Test
    public void testContainerClient_whenValidDetailsProvided_shouldReturnUserDetails() {
        ClientRequestDTO clientRequestDTO = ClientRequestDTO.builder()
                .firstName(faker.name().firstName())
                .lastName(faker.name().lastName())
                .dateOfBirth("1990-01-01")
                .taxNumber("123456789")
                .email(faker.internet().emailAddress())
                .address(faker.address().streetAddress())
                .phoneNumber(faker.phoneNumber().phoneNumber())
                .build();

        ClientResponseDTO response = given()
                .body(clientRequestDTO)
                .when()
                .post(API_CLIENT_V1 + ADD_CLIENT)
                .then()
                .extract()
                .as(ClientResponseDTO.class);

        try (AutoCloseableSoftAssertions softly = new AutoCloseableSoftAssertions()) {
            softly.assertThat(response.getClientId())
                    .as("Client ID")
                    .isNotNull();
            softly.assertThat(response.getFirstName())
                    .as("First Name is incorrect")
                    .isEqualTo(clientRequestDTO.getFirstName());
            softly.assertThat(response.getLastName())
                    .as("Last Name is incorrect")
                    .isEqualTo(clientRequestDTO.getLastName());
            softly.assertThat(response.getDateOfBirth())
                    .as("Date of Birth is incorrect")
                    .isEqualTo(clientRequestDTO.getDateOfBirth());
            softly.assertThat(response.getTaxNumber())
                    .as("Tax Number is incorrect")
                    .isEqualTo(clientRequestDTO.getTaxNumber());
            softly.assertThat(response.getEmail())
                    .as("Email is incorrect")
                    .isEqualTo(clientRequestDTO.getEmail());
            softly.assertThat(response.getPhoneNumber())
                    .as("Phone Number is incorrect")
                    .isEqualTo(clientRequestDTO.getPhoneNumber());
            softly.assertThat(response.getAddress())
                    .as("Address is incorrect")
                    .isEqualTo(clientRequestDTO.getAddress());
            softly.assertThat(response.getCreatedAt())
                    .as("Created At is not null")
                    .isNotNull();
            softly.assertThat(response.getUUID())
                    .as("UUID is null")
                    .isNotNull();
            softly.assertAll();
        }
    }
}
