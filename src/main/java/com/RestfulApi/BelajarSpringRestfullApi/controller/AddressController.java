package com.RestfulApi.BelajarSpringRestfullApi.controller;

import com.RestfulApi.BelajarSpringRestfullApi.Entity.Users;
import com.RestfulApi.BelajarSpringRestfullApi.model.AddressesResponse;
import com.RestfulApi.BelajarSpringRestfullApi.model.CreateAddressesRequest;
import com.RestfulApi.BelajarSpringRestfullApi.model.UpdateAddressesRequest;
import com.RestfulApi.BelajarSpringRestfullApi.model.WebResponse;
import com.RestfulApi.BelajarSpringRestfullApi.service.AddressesService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class AddressController {

    private final AddressesService addressesService;

    public AddressController(AddressesService addressesService) {
        this.addressesService = addressesService;
    }

    @PostMapping(
            path = "/api/contact/{contactId}/addresses",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE

    )
    public WebResponse<AddressesResponse> create(Users users,
                                                 @RequestBody CreateAddressesRequest request,
                                                 @PathVariable("contactId") String contactId) {

        request.setContactId(contactId);
        AddressesResponse addressesResponse = addressesService.create(users, request);

        return WebResponse.<AddressesResponse>builder().data(addressesResponse).build();
    }


    @GetMapping(
            path = "/api/contact/{contactId}/addresses/{addressId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<AddressesResponse> get(Users users,
                                              @PathVariable("contactId") String contactId,
                                              @PathVariable("addressId") String addressId) {

        AddressesResponse addressesResponse = addressesService.get(users, contactId, addressId);

        return WebResponse.<AddressesResponse>builder().data(addressesResponse).build();
    }

    @PutMapping(
            path = "/api/contact/{contactId}/addresses/{addressId}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE

    )
    public WebResponse<AddressesResponse> update(Users users,
                                                 @RequestBody UpdateAddressesRequest request,
                                                 @PathVariable("contactId") String contactId,
                                                 @PathVariable("addressId") String addressId) {

        request.setContactId(contactId);
        request.setAddressId(addressId);
        AddressesResponse addressesResponse = addressesService.update(users, request);

        return WebResponse.<AddressesResponse>builder().data(addressesResponse).build();
    }

    @DeleteMapping(
            path = "/api/contact/{contactId}/addresses/{addressId}",
            produces = MediaType.APPLICATION_JSON_VALUE

    )
    public WebResponse<String> remove(Users users,
                                      @PathVariable("contactId") String contactId,
                                      @PathVariable("addressId") String addressId) {

        addressesService.remove(users, contactId, addressId);

        return WebResponse.<String>builder().data("OK").build();

    }

    @GetMapping(
            path = "/api/contact/{contactId}/addresses",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<List<AddressesResponse>> list(Users users,
                                                    @PathVariable("contactId") String contactId){

        List<AddressesResponse> addressesResponses = addressesService.list(users, contactId);

        return WebResponse.<List<AddressesResponse>>builder().data(addressesResponses).build();
    }
}
