package com.ihomziak.clientaccountms.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "client")
@Setter
@Getter
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "client_id")
    private long clientId;

    @Column(name = "first_name")
    @NotNull
    private String firstName;

    @Column(name = "last_name")
    @NotNull
    private String lastName;

    @Column(name = "date_of_birth")
    @NotNull
    private String dateOfBirth;

    @Column(name = "tax_number")
    @NotNull
    private String taxNumber;

    @Column(name = "email", unique = true)
    @NotNull
    private String email;

    @Column(name = "phone_number")
    @NotNull
    private String phoneNumber;

    @Column(name = "address")
    @NotNull
    private String address;

    @Column(name = "created_at")
    @CreatedDate
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @LastModifiedDate
    private LocalDateTime updatedAt;

    @Column(name = "uuid")
    private String UUID;

    @OneToMany(mappedBy = "client")
    private List<Account> account;
}
