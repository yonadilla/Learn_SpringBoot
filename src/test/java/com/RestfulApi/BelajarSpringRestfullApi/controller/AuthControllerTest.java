package com.RestfulApi.BelajarSpringRestfullApi.controller;

import com.RestfulApi.BelajarSpringRestfullApi.Entity.Users;
import com.RestfulApi.BelajarSpringRestfullApi.model.LoginUserRequest;
import com.RestfulApi.BelajarSpringRestfullApi.model.TokenResponse;
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
class AuthControllerTest {

    private final MockMvc mockMvc;
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;

    @Autowired
    public AuthControllerTest(MockMvc mockMvc, UserRepository userRepository, ObjectMapper objectMapper) {
        this.mockMvc = mockMvc;
        this.userRepository = userRepository;
        this.objectMapper = objectMapper;
    }


    @BeforeEach
    void setUp(){
        userRepository.deleteAll();
    }

    @Test
    void loginFailedNotFound() throws Exception {

        LoginUserRequest request = new LoginUserRequest();
        request.setUsername("test");
        request.setPassword("test");

        mockMvc.perform(
                post("/api/auth/login")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isUnauthorized()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNotNull(response.getError());
        });
    }

    @Test
    void loginFailedWrongPassword() throws Exception {
        Users users = new Users();
        users.setName("test");
        users.setUsername("test");
        users.setPassword(BCrypt.hashpw("test", BCrypt.gensalt()));

        LoginUserRequest request = new LoginUserRequest();
        request.setUsername("test");
        request.setPassword("salah");

        mockMvc.perform(
                post("/api/auth/login")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isUnauthorized()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNotNull(response.getError());
        });
    }

    @Test
    void loginSuccess() throws Exception {
        Users users = new Users();
        users.setName("test");
        users.setUsername("test");
        users.setPassword(BCrypt.hashpw("test", BCrypt.gensalt()));
        userRepository.save(users);

        LoginUserRequest request = new LoginUserRequest();
        request.setUsername("test");
        request.setPassword("test");

        mockMvc.perform(
                post("/api/auth/login")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<TokenResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNull(response.getError());
            assertNotNull(response.getData().getToken());
            assertNotNull(response.getData().getExpiredAt());

            Users userDB = userRepository.findById("test").orElse(null);
            assertNotNull(userDB);
            assertEquals(userDB.getToken(), response.getData().getToken());
            assertEquals(userDB.getExpired_at(), response.getData().getExpiredAt());

        });
    }

    @Test
    void logoutFailed() throws Exception{
        mockMvc.perform(
                delete("/api/auth/logout")
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
    void logoutSuccess() throws Exception{
        Users users = new Users();
        users.setName("Test");
        users.setUsername("test");
        users.setPassword(BCrypt.hashpw("test", BCrypt.gensalt()));
        users.setToken("test");
        users.setExpired_at(System.currentTimeMillis() + 1000000000L);
        userRepository.save(users);

        mockMvc.perform(
                delete("/api/auth/logout")
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN", "test")
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNull(response.getError());
            assertEquals("OK", response.getData());

            Users userDb = userRepository.findById("test").orElse(null);
            assertNotNull(userDb);
            assertNull(userDb.getToken());
            assertNull(userDb.getExpired_at());
        });
    }
}