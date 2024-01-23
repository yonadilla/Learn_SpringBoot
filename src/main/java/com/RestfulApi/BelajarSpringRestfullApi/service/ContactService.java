package com.RestfulApi.BelajarSpringRestfullApi.service;

import com.RestfulApi.BelajarSpringRestfullApi.Entity.Contact;
import com.RestfulApi.BelajarSpringRestfullApi.Entity.Users;
import com.RestfulApi.BelajarSpringRestfullApi.model.ContactResponse;
import com.RestfulApi.BelajarSpringRestfullApi.model.CreateContactRequest;
import com.RestfulApi.BelajarSpringRestfullApi.model.SearchContactRequest;
import com.RestfulApi.BelajarSpringRestfullApi.model.UpdateContactRequest;
import com.RestfulApi.BelajarSpringRestfullApi.repository.ContactRepository;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ContactService {

    private final ContactRepository contactRepository;
    private final ValidationService validationService;

    public ContactService(ContactRepository contactRepository, ValidationService validationService) {
        this.contactRepository = contactRepository;
        this.validationService = validationService;
    }

    @Transactional
    public ContactResponse create(Users users, CreateContactRequest contactRequest){
        validationService.validate(contactRequest);

        Contact contact = new Contact();
        contact.setId(UUID.randomUUID().toString());
        contact.setFirstName(contactRequest.getFirstName());
        contact.setLastName(contactRequest.getLastName());
        contact.setEmail(contactRequest.getEmail());
        contact.setPhone(contactRequest.getPhone());
        contact.setUsers(users);

        contactRepository.save(contact);

        return toContactResponse(contact);
    }

    public ContactResponse toContactResponse(Contact contact){
        return ContactResponse.builder()
                .Id(contact.getId())
                .firstName(contact.getFirstName())
                .lastName(contact.getLastName())
                .email(contact.getEmail())
                .phone(contact.getPhone())
                .build();
    }

    @Transactional(readOnly = true)
    public ContactResponse get(Users users, String id){
        Contact contact = contactRepository.findByUsersAndId(users, id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Contact not found"));

        return toContactResponse(contact);
    }

    @Transactional
    public ContactResponse update(Users users, UpdateContactRequest request){
        validationService.validate(request);
        Contact contact = contactRepository.findByUsersAndId(users, request.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Contact not found"));

        contact.setFirstName(request.getFirstName());
        contact.setLastName(request.getLastName());
        contact.setEmail(request.getEmail());
        contact.setPhone(request.getPhone());
        contactRepository.save(contact);

        return toContactResponse(contact);
    }

    @Transactional
    public void delete(Users users, String contact_id){
        Contact contact = contactRepository.findByUsersAndId(users, contact_id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Contact not found"));

        contactRepository.delete(contact);
    }

    @Transactional(readOnly = true)
    public Page<ContactResponse> search(Users users, SearchContactRequest request){
        Specification<Contact> specification = (root, query, builder) ->{
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(builder.equal(root.get("users"), users));

            if (Objects.nonNull(request.getName())){
                predicates.add(builder.or(
                        builder.like(root.get("firstName"), "%"+request.getName() +"%"),
                        builder.like(root.get("lastName"), "%"+request.getName() +"%")
                ));
            }

            if (Objects.nonNull(request.getEmail())) {
                predicates.add(builder.like(root.get("email"), "%"+request.getEmail() +"%"));
            }

            if (Objects.nonNull(request.getPhone())) {
                predicates.add(builder.like(root.get("phone"), "%"+request.getPhone() +"%"));

            }
                return query.where(predicates.toArray(new Predicate[]{})).getRestriction();
        };

        Pageable pageable = PageRequest.of(request.getPage(), request.getSize());
        Page<Contact> contacts = contactRepository.findAll(specification, pageable);
        List<ContactResponse> contactResponses = contacts.getContent().stream()
                .map(this::toContactResponse)
                .toList();

        return new PageImpl<>(contactResponses, pageable, contacts.getTotalElements());
    }
}
