package com.RestfulApi.BelajarSpringRestfullApi.repository;

import com.RestfulApi.BelajarSpringRestfullApi.Entity.Contact;
import com.RestfulApi.BelajarSpringRestfullApi.Entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ContactRepository extends JpaRepository <Contact, String>, JpaSpecificationExecutor<Contact> {
    Optional<Contact> findByUsersAndId(Users users, String id);

    Optional<Contact> findByUsersAndFirstName(Users users, String username);
}
