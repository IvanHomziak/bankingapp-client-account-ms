package com.ihomziak.clientaccountms.entity;

import com.ihomziak.bankingapp.common.utils.AccountType;
import net.datafaker.Faker;
import org.assertj.core.api.AutoCloseableSoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@DataJpaTest
@ActiveProfiles("test")
class AccountEntityIntegrationTest {

    @Autowired
    private TestEntityManager testEntityManager;

    private Faker faker;

    @BeforeEach
    void setUp() {
        faker = new Faker();
    }

    @Test
    void testAccountEntity_whenValidDataProvided_shouldPersistAndRetrieveCorrectly() {
        // Arrange - first create a Client (because Account requires it)
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

        Client persistedClient = testEntityManager.persistAndFlush(client);

        // Arrange - create Account entity
        Account account = new Account();
        account.setAccountNumber("ACC-" + faker.number().digits(10));
        account.setAccountType(AccountType.CHECKING);
        account.setBalance(BigDecimal.valueOf(1000.00));
        account.setCreatedAt(LocalDateTime.now());
        account.setLastUpdate(LocalDateTime.now());
        account.setUUID(UUID.randomUUID().toString());
        account.setClient(persistedClient);

        // Act
        Account storedAccount = testEntityManager.persistAndFlush(account);

        // Assert
        try (AutoCloseableSoftAssertions softly = new AutoCloseableSoftAssertions()) {
            softly.assertThat(storedAccount.getAccountId())
                    .as("Account ID should be generated")
                    .isNotNull()
                    .isPositive();

            softly.assertThat(storedAccount.getAccountNumber())
                    .as("Account number should match")
                    .isEqualTo(account.getAccountNumber());

            softly.assertThat(storedAccount.getAccountType())
                    .as("Account type should match")
                    .isEqualTo(account.getAccountType());

            softly.assertThat(storedAccount.getBalance())
                    .as("Account balance should match")
                    .isEqualTo(account.getBalance());

            softly.assertThat(storedAccount.getUUID())
                    .as("UUID should match")
                    .isEqualTo(account.getUUID());

            softly.assertThat(storedAccount.getClient())
                    .as("Client reference should not be null")
                    .isNotNull();

            softly.assertThat(storedAccount.getClient().getClientId())
                    .as("Client ID should match")
                    .isEqualTo(persistedClient.getClientId());
        }
    }
}
