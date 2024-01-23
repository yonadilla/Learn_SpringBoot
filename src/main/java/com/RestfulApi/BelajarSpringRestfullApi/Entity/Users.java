package com.RestfulApi.BelajarSpringRestfullApi.Entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(schema = "restfullapi", name = "users")
public class Users {

    @Id
    private String username;

    private String password;

    private String name;

    private String token;

    @Column(name = "token_expired_at")
    private Long expired_at;

    @OneToMany(mappedBy = "users")
    private List<Contact> contacs;
}
