package com.RestfulApi.BelajarSpringRestfullApi.service;

import com.RestfulApi.BelajarSpringRestfullApi.Entity.Users;
import com.RestfulApi.BelajarSpringRestfullApi.model.RegisterUserRequest;
import com.RestfulApi.BelajarSpringRestfullApi.model.UpdateUserRequest;
import com.RestfulApi.BelajarSpringRestfullApi.model.UsersResponse;
import com.RestfulApi.BelajarSpringRestfullApi.repository.UserRepository;
import com.RestfulApi.BelajarSpringRestfullApi.security.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;

@Service
public class UserService {

    private final UserRepository repository;

    private final ValidationService validator;
    @Autowired
    public UserService(UserRepository repository, ValidationService validator) {
        this.repository = repository;
        this.validator = validator;
    }

    @Transactional
    public void register(RegisterUserRequest request){
        validator.validate(request);

        if (repository.findById(request.getUsername()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username already register");
        }

        Users users = new Users();
        users.setUsername(request.getUsername());
        users.setPassword(BCrypt.hashpw(request.getPassword(), BCrypt.gensalt()));
        users.setName(request.getName());

        repository.save(users);

    }

    public UsersResponse get(Users users){
        return UsersResponse.builder()
                .username(users.getUsername())
                .name(users.getUsername())
                .build();
    }

    @Transactional
    public UsersResponse update(Users users, UpdateUserRequest request){
        validator.validate(request);

        if (Objects.nonNull(request.getName())){
            users.setName(request.getName());
        }

        if(Objects.nonNull(request.getPassword())){
            users.setPassword(BCrypt.hashpw(request.getPassword(), BCrypt.gensalt()));
        }

        repository.save(users);

        return UsersResponse.builder()
                .name(users.getName())
                .username(users.getUsername())
                .build();
    }





}
