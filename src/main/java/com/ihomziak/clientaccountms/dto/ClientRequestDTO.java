package com.ihomziak.clientaccountms.dto;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ClientRequestDTO {

    private String firstName;
    private String lastName;
    private String dateOfBirth;
    private String taxNumber;
    @Email
    private String email;
    private String phoneNumber;
    private String address;
}
