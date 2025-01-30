package com.ihomziak.clientaccountms.dto;

import lombok.Setter;
import lombok.Getter;

import java.util.Objects;

@Setter
@Getter
public class AccountHolderDTO {

    private String UUID;
    private String firstName;
    private String lastName;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        AccountHolderDTO that = (AccountHolderDTO) o;
        return Objects.equals(UUID, that.UUID) && Objects.equals(firstName, that.firstName) && Objects.equals(lastName, that.lastName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(UUID, firstName, lastName);
    }

    @Override
    public String toString() {
        return "AccountHolderDTO{" +
                "UUID='" + UUID + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }
}
