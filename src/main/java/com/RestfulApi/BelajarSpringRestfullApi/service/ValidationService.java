package com.RestfulApi.BelajarSpringRestfullApi.service;

import com.RestfulApi.BelajarSpringRestfullApi.model.RegisterUserRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class ValidationService {

    private final Validator validator;
    public ValidationService(Validator validator) {
        this.validator = validator;
    }

    public void validate(Object request){
        Set<ConstraintViolation<Object>> validate = validator.validate(request);
        if (validate.size() != 0){
            throw new ConstraintViolationException(validate);
        }
    }

}
