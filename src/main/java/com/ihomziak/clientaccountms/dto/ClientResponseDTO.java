package com.ihomziak.clientaccountms.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
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
}
