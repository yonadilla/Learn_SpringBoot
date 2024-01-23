package com.RestfulApi.BelajarSpringRestfullApi.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UsersResponse {

    private String username;

    private String name;
}
