package com.ihomziak.clientaccountms.dto;

import lombok.Setter;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Objects;

@Setter
@Getter
public class ClientResponseDTO {

    private long clientId;
    private String firstName;
    private String lastName;
    private String dateOfBirth;
    private String taxNumber;
    private String email;
    private String phoneNumber;
    private String address;
    private LocalDateTime createdAt;
    private LocalDateTime updateAt;
    private String UUID;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ClientResponseDTO that = (ClientResponseDTO) o;
        return clientId == that.clientId && Objects.equals(firstName, that.firstName) && Objects.equals(lastName, that.lastName) && Objects.equals(dateOfBirth, that.dateOfBirth) && Objects.equals(taxNumber, that.taxNumber) && Objects.equals(email, that.email) && Objects.equals(phoneNumber, that.phoneNumber) && Objects.equals(address, that.address) && Objects.equals(createdAt, that.createdAt) && Objects.equals(updateAt, that.updateAt) && Objects.equals(UUID, that.UUID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(clientId, firstName, lastName, dateOfBirth, taxNumber, email, phoneNumber, address, createdAt, updateAt, UUID);
    }

    @Override
    public String toString() {
        return "ClientResponseDTO{" +
                "clientId=" + clientId +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", dateOfBirth='" + dateOfBirth + '\'' +
                ", taxNumber='" + taxNumber + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", address='" + address + '\'' +
                ", createdAt=" + createdAt +
                ", updateAt=" + updateAt +
                ", UUID='" + UUID + '\'' +
                '}';
    }
}
