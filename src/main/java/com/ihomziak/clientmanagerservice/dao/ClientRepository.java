package com.ihomziak.clientmanagerservice.dao;

import com.ihomziak.clientmanagerservice.entity.Client;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

    Optional<Client> findClientByTaxNumber(String taxNumber);

    Optional<Client> findClientByUUID(@NotNull String uuid);
}
