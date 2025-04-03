package com.ihomziak.clientaccountms.util;

import com.ihomziak.clientaccountms.entity.Client;
import org.springframework.data.jpa.domain.Specification;

public class UserSpecifications {

    public static Specification<Client> hasFirstName(String firstName) {
        return (root, query, cb) ->
                (firstName == null || firstName.isBlank()) ? null :
                        cb.like(cb.lower(root.get("firstName")), "%" + firstName.toLowerCase() + "%");
    }

    public static Specification<Client> hasLastName(String lastName) {
        return (root, query, cb) ->
                (lastName == null || lastName.isBlank()) ? null :
                        cb.like(cb.lower(root.get("lastName")), "%" + lastName.toLowerCase() + "%");
    }

    public static Specification<Client> hasEmail(String email) {
        return (root, query, cb) ->
                (email == null || email.isBlank()) ? null :
                        cb.like(cb.lower(root.get("email")), "%" + email.toLowerCase() + "%");
    }
}

