package com.RestfulApi.BelajarSpringRestfullApi.controller;

import com.RestfulApi.BelajarSpringRestfullApi.Entity.Users;
import com.RestfulApi.BelajarSpringRestfullApi.model.RegisterUserRequest;
import com.RestfulApi.BelajarSpringRestfullApi.model.UpdateUserRequest;
import com.RestfulApi.BelajarSpringRestfullApi.model.UsersResponse;
import com.RestfulApi.BelajarSpringRestfullApi.model.WebResponse;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    private final MockMvc mockMvc;

    private final UserRepository userRepository;

    private final ObjectMapper objectMapper;

    @Autowired
    public UserControllerTest(MockMvc mockMvc, UserRepository userRepository, ObjectMapper objectMapper) {
        this.mockMvc = mockMvc;
        this.userRepository = userRepository;
        this.objectMapper = objectMapper;
    }

    @BeforeEach
    void setup(){
        userRepository.deleteAll();
    }

    @Test
    void testRegisterSuccess() throws Exception {

        RegisterUserRequest request = new RegisterUserRequest();
        request.setUsername("test");
        request.setPassword("rahasia");
        request.setName("Test");

        mockMvc.perform(
                post("/api/users/")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))

        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertEquals("OK", response.getData());

        });
    }

    @Test
    void testRegisterBadRequest() throws Exception {

        RegisterUserRequest request = new RegisterUserRequest();
        request.setUsername("");
        request.setPassword("");
        request.setName("");

        mockMvc.perform(
                post("/api/users/")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))

        ).andExpectAll(
                status().isBadRequest()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNotNull(response.getError());

        });
    }

    @Test
    void testRegisterDuplicate() throws Exception {
        Users users = new Users();
        users.setUsername("test");
        users.setPassword(BCrypt.hashpw("rahasia", BCrypt.gensalt()));
        users.setName("test");
        userRepository.save(users);

        RegisterUserRequest request = new RegisterUserRequest();
        request.setUsername("test");
        request.setPassword("rahasia");
        request.setName("Test");

        mockMvc.perform(
                post("/api/users/")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))

        ).andExpectAll(
                status().isBadRequest()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNotNull(response.getError());

        });
    }
    @Test
    void getUnauthorized() throws Exception{
        mockMvc.perform(
                get("/api/users/current")
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN", "not found")
        ).andExpectAll(
                status().isUnauthorized()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNotNull(response.getError());
        });
    }

    @Test
    void getUnauthorizedTokenNotSend() throws Exception{
        mockMvc.perform(
                get("/api/users/current")
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isUnauthorized()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNotNull(response.getError());
        });
    }

    @Test
    void getUsersSuccess() throws Exception{
        Users users = new Users();
        users.setUsername("test");
        users.setPassword(BCrypt.hashpw("test", BCrypt.gensalt()));
        users.setName("Test");
        users.setToken("test");
        users.setExpired_at(System.currentTimeMillis() + 1000000000L);
        userRepository.save(users);


        mockMvc.perform(
                get("/api/users/current")
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN", "test")
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<UsersResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNull(response.getError());
            assertEquals("test", response.getData().getUsername());
            assertEquals("test", response.getData().getName());
        });
    }


    @Test
    void getUsersTokenExpired() throws Exception{
        Users users = new Users();
        users.setUsername("test");
        users.setPassword(BCrypt.hashpw("test", BCrypt.gensalt()));
        users.setName("Test");
        users.setToken("test");
        users.setExpired_at(System.currentTimeMillis() - 1000000000L);
        userRepository.save(users);


        mockMvc.perform(
                get("/api/users/current")
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN", "test")
        ).andExpectAll(
                status().isUnauthorized()
        ).andDo(result -> {
            WebResponse<UsersResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNotNull(response.getError());
        });
    }

    @Test
    void updateUserUnauthorized() throws Exception{
        UpdateUserRequest userRequest = new UpdateUserRequest();

        mockMvc.perform(
                patch("/api/users/current")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(userRequest))
        ).andExpectAll(
                status().isUnauthorized()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNotNull(response.getError());
        });
    }

    @Test
    void updateUserSuccess() throws Exception{
        Users users = new Users();
        users.setUsername("test");
        users.setPassword(BCrypt.hashpw("test", BCrypt.gensalt()));
        users.setName("Test");
        users.setToken("test");
        users.setExpired_at(System.currentTimeMillis() + 1000000000L);
        userRepository.save(users);

        UpdateUserRequest userRequest = new UpdateUserRequest();
        userRequest.setName("Adi");
        userRequest.setPassword("Adi123456");

        mockMvc.perform(
                patch("/api/users/current")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(userRequest))
                        .header("X-API-TOKEN", "test")
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<UsersResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNull(response.getError());
            assertEquals("Adi", response.getData().getName());
            assertEquals("test", response.getData().getUsername());

            Users userDb = userRepository.findById("test").orElse(null);
            assertNotNull(userDb);
            assertTrue(BCrypt.checkpw("Adi123456", userDb.getPassword()));

        });
    }
}