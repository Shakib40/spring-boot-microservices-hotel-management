package com.user.service.impl;

import com.user.dto.AddressRequest;
import com.user.dto.AddressResponse;
import com.user.entity.Address;
import com.user.entity.User;
import com.user.repository.AddressRepository;
import com.user.repository.UserRepository;
import com.user.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;
    private final UserRepository userRepository;

    @Override
    public AddressResponse createAddress(AddressRequest addressRequest) {
        User user = userRepository.findById(addressRequest.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + addressRequest.getUserId()));

        Address address = Address.builder()
                .street(addressRequest.getStreet())
                .city(addressRequest.getCity())
                .state(addressRequest.getState())
                .postalCode(addressRequest.getPostalCode())
                .country(addressRequest.getCountry())
                .addressType(addressRequest.getAddressType())
                .userId(user.getId())
                .build();

        Address savedAddress = addressRepository.save(address);
        return mapToResponse(savedAddress);
    }

    @Override
    public List<AddressResponse> getAddressesByUserId(String userId) {
        return addressRepository.findByUserId(userId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public AddressResponse getAddressById(String id) {
        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Address not found with id: " + id));
        return mapToResponse(address);
    }

    @Override
    public AddressResponse updateAddress(String id, AddressRequest addressRequest) {
        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Address not found with id: " + id));

        User user = userRepository.findById(addressRequest.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + addressRequest.getUserId()));

        address.setStreet(addressRequest.getStreet());
        address.setCity(addressRequest.getCity());
        address.setState(addressRequest.getState());
        address.setPostalCode(addressRequest.getPostalCode());
        address.setCountry(addressRequest.getCountry());
        address.setAddressType(addressRequest.getAddressType());
        address.setUserId(user.getId());

        Address updatedAddress = addressRepository.save(address);
        return mapToResponse(updatedAddress);
    }

    @Override
    public void deleteAddress(String id) {
        if (!addressRepository.existsById(id)) {
            throw new RuntimeException("Address not found with id: " + id);
        }
        addressRepository.deleteById(id);
    }

    private AddressResponse mapToResponse(Address address) {
        return AddressResponse.builder()
                .id(address.getId())
                .street(address.getStreet())
                .city(address.getCity())
                .state(address.getState())
                .postalCode(address.getPostalCode())
                .country(address.getCountry())
                .addressType(address.getAddressType())
                .userId(address.getUserId())
                .build();
    }
}
