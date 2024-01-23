package com.RestfulApi.BelajarSpringRestfullApi.repository;

import com.RestfulApi.BelajarSpringRestfullApi.Entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Users, String> {


    Optional<Users> findByToken(String token);

}
