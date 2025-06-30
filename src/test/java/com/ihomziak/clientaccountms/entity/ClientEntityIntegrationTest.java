package com.ihomziak.clientaccountms.entity;

import net.datafaker.Faker;
import org.assertj.core.api.AutoCloseableSoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.UUID;

@DataJpaTest
@ActiveProfiles("test")
class ClientEntityIntegrationTest {

    @Autowired
    private TestEntityManager testEntityManager;

    private Faker faker;

    @BeforeEach
    void setUp() {
        faker = new Faker();
    }

    @Test
    void testClientEntity_whenValidUserDetailsProvided_shouldReturnStoredUserDetails() {
        // Arrange
        Client client = new Client();
        client.setFirstName(faker.name().firstName());
        client.setLastName(faker.name().lastName());
        client.setEmail(faker.internet().emailAddress());
        client.setPhoneNumber(faker.phoneNumber().phoneNumber());
        client.setAddress(faker.address().fullAddress());
        client.setCreatedAt(LocalDateTime.now());
        client.setTaxNumber("1234567890");
        client.setDateOfBirth("1990-01-01");
        client.setUUID(UUID.randomUUID().toString());

        // Act
        Client storedClient = testEntityManager.persistAndFlush(client);

        // Assert
        try (AutoCloseableSoftAssertions softly = new AutoCloseableSoftAssertions()) {
            softly.assertThat(storedClient)
                    .as("Client ID is null")
                    .isNotNull();
            softly.assertThat(storedClient.getFirstName())
                    .as("Client First Name is incorrect")
                    .isEqualTo(client.getFirstName());
            softly.assertThat(storedClient.getLastName())
                    .as("Client Last Name is incorrect")
                    .isEqualTo(client.getLastName());
            softly.assertThat(storedClient.getEmail())
                    .as("Client Email is incorrect")
                    .isEqualTo(client.getEmail());
            softly.assertThat(storedClient.getPhoneNumber())
                    .as("Client Phone Number is incorrect")
                    .isEqualTo(client.getPhoneNumber());
            softly.assertThat(storedClient.getAddress())
                    .as("Client Address is incorrect")
                    .isEqualTo(client.getAddress());
            softly.assertThat(storedClient.getCreatedAt())
                    .as("Client Created At is incorrect")
                    .isEqualTo(client.getCreatedAt());
            softly.assertThat(storedClient.getTaxNumber())
                    .as("Client Tax Number is incorrect")
                    .isEqualTo(client.getTaxNumber());
            softly.assertThat(storedClient.getDateOfBirth())
                    .as("Client Date Of Birth is incorrect")
                    .isEqualTo(client.getDateOfBirth());
        }
    }
}