package com.RestfulApi.BelajarSpringRestfullApi.controller;

import com.RestfulApi.BelajarSpringRestfullApi.Entity.Addresses;
import com.RestfulApi.BelajarSpringRestfullApi.Entity.Contact;
import com.RestfulApi.BelajarSpringRestfullApi.Entity.Users;
import com.RestfulApi.BelajarSpringRestfullApi.model.AddressesResponse;
import com.RestfulApi.BelajarSpringRestfullApi.model.CreateAddressesRequest;
import com.RestfulApi.BelajarSpringRestfullApi.model.UpdateAddressesRequest;
import com.RestfulApi.BelajarSpringRestfullApi.model.WebResponse;
import com.RestfulApi.BelajarSpringRestfullApi.repository.AddressesRepository;
import com.RestfulApi.BelajarSpringRestfullApi.repository.ContactRepository;
import com.RestfulApi.BelajarSpringRestfullApi.repository.UserRepository;
import com.RestfulApi.BelajarSpringRestfullApi.security.BCrypt;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;


import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AddressControllerTest {

    private final UserRepository userRepository;

    private final ContactRepository contactRepository;

    private final AddressesRepository addressesRepository;

    private final MockMvc mockMvc;

    private final ObjectMapper objectMapper;

    @Autowired
    public AddressControllerTest(UserRepository userRepository, ContactRepository contactRepository, AddressesRepository addressesRepository, MockMvc mockMvc, ObjectMapper objectMapper) {
        this.userRepository = userRepository;
        this.contactRepository = contactRepository;
        this.addressesRepository = addressesRepository;
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
    }

    @BeforeEach
    void setUp() {
        addressesRepository.deleteAll();
        contactRepository.deleteAll();
        userRepository.deleteAll();

        Users users = new Users();
        users.setName("test");
        users.setUsername("test");
        users.setPassword(BCrypt.hashpw("test", BCrypt.gensalt()));
        users.setToken("test");
        users.setExpired_at(System.currentTimeMillis() + 10000000000L);
        userRepository.save(users);

        Contact contact = new Contact();
        contact.setId("test");
        contact.setUsers(users);
        contact.setFirstName("Yon");
        contact.setLastName("Adi");
        contact.setEmail("yonadi123@gmail.com");
        contact.setPhone("123456789");
        contactRepository.save(contact);
    }

    @Test
    void createAddressBadRequest() throws Exception {
        CreateAddressesRequest request = new CreateAddressesRequest();
        request.setCountry("");

        mockMvc.perform(
                post("/api/contact/test/addresses")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header("X-API-TOKEN", "test")
        ).andExpectAll(
                status().isBadRequest()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNotNull(response.getError());
        });
    }

    @Test
    void createAddressSuccess() throws Exception {
        CreateAddressesRequest request = new CreateAddressesRequest();
        request.setStreet("Jalan");
        request.setCity("Blora");
        request.setProvince("jawaTengah");
        request.setCountry("Indonesia");
        request.setPostalCode("58381");

        mockMvc.perform(
                post("/api/contact/test/addresses")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header("X-API-TOKEN", "test")
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<AddressesResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNull(response.getError());
            assertEquals(request.getStreet(), response.getData().getStreet());
            assertEquals(request.getCity(), response.getData().getCity());
            assertEquals(request.getProvince(), response.getData().getProvince());
            assertEquals(request.getCountry(), response.getData().getCountry());
            assertEquals(request.getPostalCode(), response.getData().getPostalCode());


            assertTrue(addressesRepository.existsById(response.getData().getId()));
        });
    }

    @Test
    void getAddressSuccess() throws Exception {
        Contact contact = contactRepository.findById("test").orElseThrow();

        Addresses addresses = new Addresses();
        addresses.setId("test");
        addresses.setContact(contact);
        addresses.setStreet("Jalan");
        addresses.setCity("Blora");
        addresses.setProvince("jawaTengah");
        addresses.setCountry("Indonesia");
        addresses.setPostalCode("58381");
        addressesRepository.save(addresses);

        mockMvc.perform(
                get("/api/contact/test/addresses/test")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN", "test")
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<AddressesResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNull(response.getError());
            assertEquals(addresses.getId(), response.getData().getId());
            assertEquals(addresses.getStreet(), response.getData().getStreet());
            assertEquals(addresses.getCity(), response.getData().getCity());
            assertEquals(addresses.getProvince(), response.getData().getProvince());
            assertEquals(addresses.getCountry(), response.getData().getCountry());
            assertEquals(addresses.getPostalCode(), response.getData().getPostalCode());
        });
    }

    @Test
    void getAddressNotFound() throws Exception {


        mockMvc.perform(
                get("/api/contact/test/addresses/test")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN", "test")
        ).andExpectAll(
                status().isNotFound()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNotNull(response.getError());
        });
    }

    @Test
    void updateAddressBadRequest() throws Exception {
        UpdateAddressesRequest request = new UpdateAddressesRequest();
        request.setCountry("");

        mockMvc.perform(
                put("/api/contact/test/addresses/test")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header("X-API-TOKEN", "test")
        ).andExpectAll(
                status().isBadRequest()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNotNull(response.getError());
        });

    }

    @Test
    void updateAddressSuccess() throws Exception {
        Contact contact = contactRepository.findById("test").orElseThrow();

        Addresses addresses = new Addresses();
        addresses.setId("test");
        addresses.setContact(contact);
        addresses.setStreet("Lama");
        addresses.setCity("Lama");
        addresses.setProvince("Lama");
        addresses.setCountry("Lama");
        addresses.setPostalCode("123313");
        addressesRepository.save(addresses);

        UpdateAddressesRequest request = new UpdateAddressesRequest();
        request.setStreet("Jalan");
        request.setCity("Blora");
        request.setProvince("jawaTengah");
        request.setCountry("Indonesia");
        request.setPostalCode("58381");

        mockMvc.perform(
                put("/api/contact/test/addresses/test")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header("X-API-TOKEN", "test")
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<AddressesResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNull(response.getError());
            assertEquals(request.getStreet(), response.getData().getStreet());
            assertEquals(request.getCity(), response.getData().getCity());
            assertEquals(request.getProvince(), response.getData().getProvince());
            assertEquals(request.getCountry(), response.getData().getCountry());
            assertEquals(request.getPostalCode(), response.getData().getPostalCode());


            assertTrue(addressesRepository.existsById(response.getData().getId()));
        });
    }

    @Test
    void deleteAddressNotFound() throws Exception {


        mockMvc.perform(
                delete("/api/contact/test/addresses/test")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN", "test")
        ).andExpectAll(
                status().isNotFound()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNotNull(response.getError());
        });
    }

    @Test
    void deleteAddressSuccess() throws Exception {
        Contact contact = contactRepository.findById("test").orElseThrow();

        Addresses addresses = new Addresses();
        addresses.setId("test");
        addresses.setContact(contact);
        addresses.setStreet("Jalan");
        addresses.setCity("Blora");
        addresses.setProvince("jawaTengah");
        addresses.setCountry("Indonesia");
        addresses.setPostalCode("58381");
        addressesRepository.save(addresses);

        mockMvc.perform(
                delete("/api/contact/test/addresses/test")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN", "test")
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNull(response.getError());
            assertEquals("OK", response.getData());

            assertFalse(addressesRepository.existsById("test"));
        });
    }

    @Test
    void listAddressNotFound() throws Exception {

        mockMvc.perform(
                get("/api/contact/salah/addresses")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN", "test")
        ).andExpectAll(
                status().isNotFound()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNotNull(response.getError());
        });
    }

    @Test
    void listAddressSuccess() throws Exception {
        Contact contact = contactRepository.findById("test").orElseThrow();

        for (int i = 0; i < 5; i++) {
            Addresses addresses = new Addresses();
            addresses.setId("test-" + i);
            addresses.setContact(contact);
            addresses.setStreet("Jalan");
            addresses.setCity("Blora");
            addresses.setProvince("jawaTengah");
            addresses.setCountry("Indonesia");
            addresses.setPostalCode("58381");
            addressesRepository.save(addresses);
        }

        mockMvc.perform(
                get("/api/contact/test/addresses")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN", "test")
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<List<AddressesResponse>> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNull(response.getError());
            assertEquals(5, response.getData().size());
        });
    }
}