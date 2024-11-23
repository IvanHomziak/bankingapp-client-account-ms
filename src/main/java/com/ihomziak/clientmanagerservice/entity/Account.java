package com.ihomziak.clientmanagerservice.entity;

import com.ihomziak.transactioncommon.utils.AccountType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Entity
@Table(name = "account")
@Setter
@Getter
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id")
    private long accountId;

    @Column(name = "account_number")
    private String accountNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "account_type")
    private AccountType accountType;

    @Column(name = "balance")
    private double balance;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "last_update")
    private LocalDateTime lastUpdate;

    @NotNull
    @Column(name = "uuid")
    private String UUID;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @Override
    public String toString() {
        return "Account{" +
                "accountId=" + accountId +
                ", accountNumber='" + accountNumber + '\'' +
                ", accountType=" + accountType +
                ", balance=" + balance +
                ", createdAt=" + createdAt +
                ", lastUpdate=" + lastUpdate +
                ", UUID='" + UUID + '\'' +
                ", clientUUID=" + (client != null ? client.getUUID() : "null") +
                '}';
    }
}