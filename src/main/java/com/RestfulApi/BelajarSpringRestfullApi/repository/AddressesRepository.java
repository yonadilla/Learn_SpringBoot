package com.RestfulApi.BelajarSpringRestfullApi.repository;

import com.RestfulApi.BelajarSpringRestfullApi.Entity.Addresses;
import com.RestfulApi.BelajarSpringRestfullApi.Entity.Contact;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AddressesRepository extends JpaRepository<Addresses, String> {

    List<Addresses> findAllByContact(Contact contact);
    Optional<Addresses> findByContactAndId(Contact contact, String id);
}
