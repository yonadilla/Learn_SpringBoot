package com.RestfulApi.BelajarSpringRestfullApi.service;

import com.RestfulApi.BelajarSpringRestfullApi.Entity.Users;
import com.RestfulApi.BelajarSpringRestfullApi.model.LoginUserRequest;
import com.RestfulApi.BelajarSpringRestfullApi.model.TokenResponse;
import com.RestfulApi.BelajarSpringRestfullApi.repository.UserRepository;
import com.RestfulApi.BelajarSpringRestfullApi.security.BCrypt;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final ValidationService validationService;

    public AuthService(UserRepository userRepository, ValidationService validationService) {
        this.userRepository = userRepository;
        this.validationService = validationService;
    }

    @Transactional
    public TokenResponse login (LoginUserRequest request){
        validationService.validate(request);

        Users users = userRepository.findById(request.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Username or Password wrong"));

        if (BCrypt.checkpw(request.getPassword(), users.getPassword())){
            users.setToken(UUID.randomUUID().toString());
            users.setExpired_at(next30Day());
            userRepository.save(users);

            return TokenResponse.builder()
                    .token(users.getToken())
                    .expiredAt(users.getExpired_at())
                    .build();
        }else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Username or Password wrong");
        }
    }

    private Long next30Day(){
        return System.currentTimeMillis() + (1000 * 16 * 24 * 30);
    }

    @Transactional
    public void logout(Users users){
        users.setToken(null);
        users.setExpired_at(null);

        userRepository.save(users);
    }
}
