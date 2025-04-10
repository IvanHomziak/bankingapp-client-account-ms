package com.ihomziak.clientaccountms.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Getter;

import java.util.Objects;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountHolderDTO {

    private String UUID;
    private String firstName;
    private String lastName;
}
