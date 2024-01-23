package com.RestfulApi.BelajarSpringRestfullApi.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddressesResponse {

    private String Id;

    private String street;

    private String city;

    private String province;

    private String country;

    private String postalCode;

}
