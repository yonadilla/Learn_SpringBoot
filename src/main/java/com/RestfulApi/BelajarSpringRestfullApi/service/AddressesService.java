package com.RestfulApi.BelajarSpringRestfullApi.service;

import com.RestfulApi.BelajarSpringRestfullApi.Entity.Addresses;
import com.RestfulApi.BelajarSpringRestfullApi.Entity.Contact;
import com.RestfulApi.BelajarSpringRestfullApi.Entity.Users;
import com.RestfulApi.BelajarSpringRestfullApi.model.AddressesResponse;
import com.RestfulApi.BelajarSpringRestfullApi.model.CreateAddressesRequest;
import com.RestfulApi.BelajarSpringRestfullApi.model.UpdateAddressesRequest;
import com.RestfulApi.BelajarSpringRestfullApi.repository.AddressesRepository;
import com.RestfulApi.BelajarSpringRestfullApi.repository.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
public class AddressesService {

    private final ContactRepository contactRepository;

    private final AddressesRepository addressesRepository;

    private final ValidationService validationService;


    public AddressesService(ContactRepository contactRepository, AddressesRepository addressesRepository, ValidationService validationService) {
        this.contactRepository = contactRepository;
        this.addressesRepository = addressesRepository;
        this.validationService = validationService;
    }

    @Transactional
    public AddressesResponse create(Users users, CreateAddressesRequest request){
        validationService.validate(request);

        Contact contact = contactRepository.findByUsersAndId(users, request.getContactId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Contact is not found"));

        Addresses addresses = new Addresses();
        addresses.setId(UUID.randomUUID().toString());
        addresses.setContact(contact);
        addresses.setStreet(request.getStreet());
        addresses.setCity(request.getCity());
        addresses.setProvince(request.getProvince());
        addresses.setCountry(request.getCountry());
        addresses.setPostalCode(request.getPostalCode());

        addressesRepository.save(addresses);

        return toAddressResponse(addresses);
    }

    private AddressesResponse toAddressResponse(Addresses addresses) {
        return AddressesResponse.builder()
                .Id(addresses.getId())
                .street(addresses.getStreet())
                .city(addresses.getCity())
                .province(addresses.getProvince())
                .country(addresses.getCountry())
                .country(addresses.getCountry())
                .postalCode(addresses.getPostalCode())
                .build();
    }

    @Transactional
    public AddressesResponse get(Users users, String contactId, String addressId){
        Contact contact = contactRepository.findByUsersAndId(users, contactId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Contact not found"));

        Addresses addresses = addressesRepository.findByContactAndId(contact, addressId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Address not found"));

        return toAddressResponse(addresses);
    }

    @Transactional
    public AddressesResponse update(Users users, UpdateAddressesRequest request){
        validationService.validate(request);

        Contact contact = contactRepository.findByUsersAndId(users, request.getContactId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Contact not found"));

        Addresses addresses = addressesRepository.findByContactAndId(contact, request.getAddressId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Address not found"));

        addresses.setStreet(request.getStreet());
        addresses.setCity(request.getCity());
        addresses.setProvince(request.getProvince());
        addresses.setCountry(request.getCountry());
        addresses.setPostalCode(request.getPostalCode());
        addressesRepository.save(addresses);

        return toAddressResponse(addresses);
    }

    @Transactional
    public void remove(Users users, String contactId, String addressId){
        Contact contact = contactRepository.findByUsersAndId(users, contactId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Contact not found"));

        Addresses addresses = addressesRepository.findByContactAndId(contact, addressId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Address not found"));

        addressesRepository.delete(addresses);
    }

    @Transactional(readOnly = true)
    public List<AddressesResponse> list(Users users, String contactId){
        Contact contact = contactRepository.findByUsersAndId(users, contactId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Contact not found"));

        List<Addresses> addresses = addressesRepository.findAllByContact(contact);
        return addresses.stream().map(this::toAddressResponse).toList();
    }

}
