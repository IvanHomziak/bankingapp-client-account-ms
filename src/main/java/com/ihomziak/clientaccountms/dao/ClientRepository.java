package com.ihomziak.clientaccountms.dao;

import com.ihomziak.clientaccountms.dto.LastNameCountDTO;
import com.ihomziak.clientaccountms.entity.Client;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long>, JpaSpecificationExecutor<Client> {

    Optional<Client> findClientByTaxNumber(String taxNumber);

    Optional<Client> findClientByUUID(@NotNull String uuid);

    //
    @Query("SELECT c FROM Client c WHERE LOWER(c.firstName) = LOWER(:firstName) AND LOWER(c.lastName) = LOWER(:lastName)")
    Optional<Client> findByFirstNameAndLastNameIgnoreCase(
            @Param("firstName") String firstName,
            @Param("lastName") String lastName
    );

    @Query("SELECT new com.ihomziak.clientaccountms.dto.LastNameCountDTO(c.lastName, COUNT(c)) " +
            "FROM Client c WHERE LOWER(c.lastName) LIKE LOWER(CONCAT(:lastName, '%')) " +
            "GROUP BY c.lastName ORDER BY COUNT(c) DESC")
    LastNameCountDTO countClientsByLastNameOrderDesc(@Param("lastName") String lastName);

    @Query("SELECT new com.ihomziak.clientaccountms.dto.LastNameCountDTO(c.lastName, COUNT(c)) " +
            "FROM Client c WHERE LOWER(c.lastName) LIKE LOWER(CONCAT(:lastName, '%')) " +
            "GROUP BY c.lastName ORDER BY COUNT(c) ASC")
    LastNameCountDTO countClientsByLastNameOrderAsc(@Param("lastName") String lastName);

}
