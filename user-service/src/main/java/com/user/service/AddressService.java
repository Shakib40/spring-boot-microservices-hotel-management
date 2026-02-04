package com.user.service;

import com.user.dto.AddressRequest;
import com.user.dto.AddressResponse;

import java.util.List;

public interface AddressService {
    AddressResponse createAddress(AddressRequest addressRequest);

    List<AddressResponse> getAddressesByUserId(String userId);

    AddressResponse getAddressById(String id);

    AddressResponse updateAddress(String id, AddressRequest addressRequest);

    void deleteAddress(String id);
}
